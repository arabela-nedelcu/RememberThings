package com.optima.rememberthings.http;

import com.optima.rememberthings.activities.LoginActivity;
import com.optima.rememberthings.http.interfaces.WeatherAPIService;
import com.optima.rememberthings.utils.Globals;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


@Module
public class RetrofitApiClient {

   private Retrofit retrofit = null;

   @Provides
   public WeatherAPIService getApi(){
       setRetrofit();
       return retrofit.create(WeatherAPIService.class);
   }

   @Provides
    public Retrofit setRetrofit(){
        if(retrofit == null){
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(Globals.WEATHER_APP_BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
