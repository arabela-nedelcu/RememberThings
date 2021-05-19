package com.optima.rememberthings.utils;

import android.content.Context;
import android.content.SharedPreferences;
import static android.content.Context.MODE_PRIVATE;

public class SharedPreferencesHelper {

    private final static String GENERAL_PREFS = "general_prefs";

    public static String getSharedPref(Context ctx, String key) {
        SharedPreferences prefs = ctx.getSharedPreferences(GENERAL_PREFS, MODE_PRIVATE);
        return prefs.getString(key, "NaN");
    }

    public static void setSharedPref(Context ctx,String key, String value) {
        SharedPreferences.Editor editor = ctx.getSharedPreferences(GENERAL_PREFS, MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static void clearSharedPreferences(Context ctx){
        SharedPreferences.Editor editor = ctx.getSharedPreferences(GENERAL_PREFS, MODE_PRIVATE).edit();
        editor.clear();
        editor.apply();;
    }
}

