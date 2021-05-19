package com.optima.rememberthings.activities;


import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.optima.rememberthings.Base.DaggerMainActivityComponent;
import com.optima.rememberthings.Base.MainActivityComponent;
import com.optima.rememberthings.R;
import com.optima.rememberthings.activities.adapters.MainListAdapter;
import com.optima.rememberthings.http.RetrofitApiClient;
import com.optima.rememberthings.http.interfaces.WeatherAPIService;
import com.optima.rememberthings.models.NotesModel;
import com.optima.rememberthings.models.Weather;
import com.optima.rememberthings.storage.LocalDbConfig;
import com.optima.rememberthings.utils.Globals;
import com.optima.rememberthings.utils.SharedPreferencesHelper;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MainListAdapter mMainListAdapter;
    private TextView tvTemperature;
    private ImageView ivWeatherIcon;
    private ImageView ivCameraImage;

    private EditText etTitle;
    private EditText etContent;
    private Realm realmDB;

    @Inject
    public WeatherAPIService mWeatherAPIService;

    private MainActivityComponent mApplicationComponent;
    private OrderedRealmCollection<NotesModel> notesModels;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realmDB = LocalDbConfig.getRealmInstance();
        mApplicationComponent = DaggerMainActivityComponent.builder().retrofitApiClient(new RetrofitApiClient()).build();
        mApplicationComponent.inject(this);
        getList();
        InitUI();
        callWeatherApi();
    }

    public void InitUI(){
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAddNoteDialog();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ivWeatherIcon = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.weather_icon_iv);
        TextView tvPersonName = (TextView) navigationView.getHeaderView(0).findViewById(R.id.person_name_tv);
        tvTemperature = (TextView) navigationView.getHeaderView(0).findViewById(R.id.temp_tv);

        String personName = SharedPreferencesHelper.getSharedPref(this,"PERSON_NAME");
        tvPersonName.setText(personName);

        RecyclerView recyclerView = findViewById(R.id.list_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mMainListAdapter = new MainListAdapter(this, realmDB, notesModels, new MainListAdapter.ItemClickListener() {
            @Override
            public void onShare(View view, int p, MainListAdapter.NotesClass notesClass) {

                    String title = notesClass.mTitle.getText().toString();
                    String body = notesClass.mBody.getText().toString();

                    StringBuilder sb = new StringBuilder();
                    sb.append(title);
                    sb.append("\n");
                    sb.append("---------------------------------------------------");
                    sb.append("\n");
                    sb.append(body);

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, sb.toString());
                    sendIntent.setType("text/plain");

                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    MainActivity.this.startActivity(shareIntent);
            }
        });
        recyclerView.setAdapter(mMainListAdapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mMainListAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mMainListAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realmDB.close();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
        GoogleSignInOptions gso = new
                GoogleSignInOptions.
                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        navigateToLoginActivity();
                    }
                });
    }

    public void navigateToLoginActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void callWeatherApi(){
        //Retrofit retrofit = RetrofitApiClient.getClient();
        //WeatherAPIService service = retrofit.create(WeatherAPIService.class);
        try{
            Call<Weather> response = mWeatherAPIService.getWeatherData("Bucharest",Globals.WEATHER_APP_ID,"metric");
            response.enqueue(new Callback<Weather>() {
                @Override
                public void onResponse(Call<Weather> call, Response<Weather> response) {
                    Weather weather = response.body();
                    double weatherTemp = weather.getWeatherTemperature().temperature;
                    int roundedWeather = (int) round(weatherTemp,0);

                    getWeatherImage(weather.getWeather().get(0).icon);
                    tvTemperature.setText(Integer.toString(roundedWeather));
                }

                @Override
                public void onFailure(Call<Weather> call, Throwable t) {
                    Log.d("WETHER", t.getMessage());
                }
            });
        }catch (Exception e){
            Log.d("WETHER", e.getMessage());
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public void getWeatherImage(String imageCode) {
        String url = "https://openweathermap.org/img/wn/"+imageCode+"@2x.png";
        Picasso.get()
                .load(url)
                .error(R.drawable.ic_launcher_foreground)
                .resize(160, 160)
                .centerCrop()
                .into(ivWeatherIcon);
    }


    public void createAddNoteDialog () {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = this.getLayoutInflater();
        View mView = inflater.inflate(R.layout.list_item_dialog, null);

        etTitle = (EditText) mView.findViewById(R.id.title_et);
        etContent = (EditText) mView.findViewById(R.id.body_et);
        ivCameraImage = (ImageView) mView.findViewById(R.id.camera_img_attach);

        Button btnAdd = (Button) mView.findViewById(R.id.add_btn);
        Button btnCancel = (Button) mView.findViewById(R.id.cancel_btn);
        ImageButton btnCamera = (ImageButton) mView.findViewById(R.id.camera_btn);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    if(TextUtils.isEmpty(etTitle.getText().toString()) || TextUtils.isEmpty(etContent.getText().toString())){
                        Toast.makeText(alertDialog.getContext(),"Trebuie sa adaugati titlu si continut!",Toast.LENGTH_SHORT);
                    }else{
                        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                        BitmapDrawable bmd= ((BitmapDrawable)ivCameraImage.getDrawable());
                        Bitmap bm = null;

                        if(bmd != null){
                            bm = bmd.getBitmap();
                        }

                        byte[] byteArray = null;

                        if(bm != null){
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
                            byteArray = stream.toByteArray();
                        }

                        byte[] finalByteArray = byteArray;
                        realmDB.executeTransaction(realm -> {
                            NotesModel notesModel = realmDB.createObject(NotesModel.class, UUID.randomUUID().toString());
                            notesModel.setTitle(etTitle.getText().toString());
                            notesModel.setBody(etContent.getText().toString());
                            notesModel.setDate(date);
                            if(finalByteArray != null){
                                notesModel.setImageBytes(finalByteArray);
                            }else{
                                notesModel.setImageBytes(null);
                            }
                        });

                        getList();

                        alertDialog.dismiss();
                    }
                }
                catch (Exception ex){
                    Log.d("Error",ex.toString());
                    Toast.makeText(alertDialog.getContext(),ex.toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                    }
                    else
                    {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    }
                }
            }
        });

        alertDialog.setView(mView);
        alertDialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = (Bitmap) Objects.requireNonNull(data.getExtras()).get("data");
            Uri uri = getImageUri(this,photo);
            Picasso.get()
                    .load(uri)
                    //.error(R.drawable.ic_launcher_foreground)
                    .resize(160, 160)
                    .centerCrop()
                    .into(ivCameraImage);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void getList(){
        notesModels = realmDB.where(NotesModel.class).findAll();
    }

}
