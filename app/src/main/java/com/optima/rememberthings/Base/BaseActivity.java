package com.optima.rememberthings.Base;


import com.optima.rememberthings.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class BaseActivity extends AppCompatActivity {

//    public void addFragment(BaseFragment fragment) {
//        FragmentManager fm = getSupportFragmentManager();
//
//        FragmentTransaction transaction = fm.beginTransaction();
//
//        Fragment oldFragment = getSupportFragmentManager().findFragmentByTag(fragment.getClass().getSimpleName());
//        if (oldFragment != null)
//            transaction.remove(oldFragment);
//
//        transaction.
//                add(R.id.container, fragment, fragment.getClass().getSimpleName())
//                .addToBackStack(null)
//                .commitAllowingStateLoss();
//    }
//
//    // do not remove any fragment unless absolutely necessary (e.g login fragment)
//    public void removeFragment(BaseFragment fragment) {
//        FragmentManager fm = getSupportFragmentManager();
//        fm.beginTransaction().remove(fragment).commitAllowingStateLoss();
//    }
//
//    public Fragment findRetainedFragment(String tag) {
//        FragmentManager fm = getSupportFragmentManager();
//        return fm.findFragmentByTag(tag);
//    }

//    @Override
//    public void onBackPressed() {
//        if ((getFragment()).onBackPressed()) // fragment will return true if it has handled the event
//            return;
//
//        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
//            getSupportFragmentManager().popBackStackImmediate();
//        } else {
//            this.finish();
//        }
//    }

//    private BaseFragment getFragment() {
//        return (BaseFragment) getSupportFragmentManager().findFragmentById(R.id.container);
//    }

    @Override
    protected void onPause() {
        super.onPause();
        //FirebaseHelper.scheduleStockSyncService(this,60*60,60*60*2);
    }
}