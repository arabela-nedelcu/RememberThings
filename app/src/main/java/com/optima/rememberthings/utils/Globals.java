package com.optima.rememberthings.utils;
import android.content.Context;
import android.graphics.Color;

import androidx.appcompat.app.AlertDialog;

public class Globals {

    public static final String WEATHER_APP_BASE_URL = "https://api.openweathermap.org/";
    public static final String WEATHER_APP_ID = "482d4f7532a6dcb3933025e4fd61e619";

    public static void CreateDialog(Context context,String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setTitle(title);
        AlertDialog dialog = builder.create();
    }

    public static final int[] COLORS = new int[] {
            Color.argb(255, 28, 160, 170),
            Color.argb(255, 99, 161, 247),
            Color.argb(255, 13, 79, 139),
            Color.argb(255, 89, 113, 173),
            Color.argb(255, 200, 213, 219),
            Color.argb(255, 99, 214, 74),
            Color.argb(255, 205, 92, 92),
            Color.argb(255, 105, 5, 98)
    };
}
