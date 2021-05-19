package com.optima.rememberthings.utils;

import android.content.Context;

public class StringUtils {

    private static Context mContext;

    public static void initialize(Context context) {
        mContext = context;
    }

    public static String getString(int id) {
        return mContext.getResources().getString(id);
    }
}