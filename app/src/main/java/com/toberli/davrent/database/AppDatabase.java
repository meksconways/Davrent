package com.toberli.davrent.database;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class AppDatabase {



    public static void addSharedWithKey(String token, Integer isAdmin, String scope, Context context) {

        SharedPreferences.Editor editor = context.getSharedPreferences("db", MODE_PRIVATE).edit();
        editor.putString("token", token);
        editor.putInt("is_admin", isAdmin);
        editor.putString("scope", scope);
        editor.apply();

    }

    public static String getScope(Context context){
        SharedPreferences prefs = context.getSharedPreferences("db", MODE_PRIVATE);
        return prefs.getString("scope", null);
    }

    public static void deleteDatabase(Context context) {
        context.getSharedPreferences("db", MODE_PRIVATE).edit().clear().apply();
    }

    public static String getToken(Context context) {

        SharedPreferences prefs = context.getSharedPreferences("db", MODE_PRIVATE);
        return prefs.getString("token", null);

    }

    public static Integer getIsAdmin(Context context){
        SharedPreferences prefs = context.getSharedPreferences("db", MODE_PRIVATE);
        return prefs.getInt("is_admin", -1);
    }


}
