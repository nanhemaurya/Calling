package com.snmboy.calling.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class SharedPrefs {

    public static void setPrefs(Context context, String key, String value){
        SharedPreferences.Editor editor = context.getSharedPreferences(Constt.SP_Call, MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getPrefs(Context context,String key){
        SharedPreferences prefs = context.getSharedPreferences(Constt.SP_Call, MODE_PRIVATE);
        return prefs.getString(key, null);//"No name defined" is the default value.
    }

}
