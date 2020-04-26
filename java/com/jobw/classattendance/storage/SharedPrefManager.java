package com.adivid.zpattendanceadmin.storage;

import android.content.Context;
import android.content.SharedPreferences;

import com.adivid.zpattendanceadmin.models.UserInfo;

public class SharedPrefManager {

    public static final String SHARED_PREF_NAME = "my_shared_pref";

    private static SharedPrefManager mInstance;
    private Context context;

    public SharedPrefManager(Context context) {
        this.context = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public void saveUser(UserInfo userInfo) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("user_id", userInfo.getUserId());
        editor.putString("name", userInfo.getName());
        editor.putString("email", userInfo.getEmail());
        editor.putString("mobile", userInfo.getMobile());
        editor.putString("designation", userInfo.getDesignation());
        editor.putString("department", userInfo.getDepartment());
        editor.putString("token_id", userInfo.getTokenId());
        editor.putString("token_value", userInfo.getTokenValue());

        editor.apply();
    }

    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString("name", null) != null;
    }

    public void clear() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

    }
}
