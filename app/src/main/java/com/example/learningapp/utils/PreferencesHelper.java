package com.example.learningapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {
    
    private static final String PREF_NAME = "LearningAppPrefs";
    private static final String KEY_NOTIFICATION_ENABLED = "notification_enabled";
    private static final String KEY_DAILY_GOAL = "daily_goal";
    
    private SharedPreferences preferences;
    
    public PreferencesHelper(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    
    public boolean isNotificationEnabled() {
        return preferences.getBoolean(KEY_NOTIFICATION_ENABLED, false);
    }
    
    public void setNotificationEnabled(boolean enabled) {
        preferences.edit().putBoolean(KEY_NOTIFICATION_ENABLED, enabled).apply();
    }
    
    public int getDailyGoal() {
        return preferences.getInt(KEY_DAILY_GOAL, 10);
    }
    
    public void setDailyGoal(int goal) {
        preferences.edit().putInt(KEY_DAILY_GOAL, goal).apply();
    }
}
