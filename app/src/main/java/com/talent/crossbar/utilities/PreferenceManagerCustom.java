package com.talent.crossbar.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManagerCustom {
    private SharedPreferences sharedPreferences;

    public PreferenceManagerCustom(Context context) {
        sharedPreferences = context.getSharedPreferences(Constants.KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    public void putString(String key, String value) {
        sharedPreferences.edit().putString(key, value).apply();
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void clearPreferences() {
        sharedPreferences.edit().clear().apply();
    }

}
