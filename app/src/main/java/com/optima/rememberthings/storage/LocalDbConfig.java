package com.optima.rememberthings.storage;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class LocalDbConfig {

    private static RealmConfiguration config;

    public static Realm getRealmInstance(){
        if(config == null){
            config = new RealmConfiguration.Builder()
                    .allowQueriesOnUiThread(true)
                    .allowWritesOnUiThread(true)
                    .build();

            return Realm.getInstance(config);
        }

        return Realm.getInstance(config);
    }
}
