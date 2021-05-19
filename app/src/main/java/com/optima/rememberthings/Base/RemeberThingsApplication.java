package com.optima.rememberthings.Base;

import android.app.Application;
import android.content.Context;
import io.realm.Realm;

public class RemeberThingsApplication extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        //StringUtils.initialize(this);
        RemeberThingsApplication.sContext = getApplicationContext();
    }
    public static Context getContext(){
        return sContext;
    }
}
