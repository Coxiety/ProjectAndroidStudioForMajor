package com.example.learningapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesHelper {
    
    private static final String PREF_NAME = "LearningAppPrefs";
    private static final String KEY_ONBOARDING_COMPLETED = "onboarding_completed";
    private static final String KEY_SELECTED_GOAL = "selected_goal";
    private static final String KEY_NOTIFICATION_ENABLED = "notification_enabled";
    private static final String KEY_NOTIFICATION_TIME = "notification_time";
    private static final String KEY_DAILY_GOAL = "daily_goal";
    
    private SharedPreferences preferences;
    
    public PreferencesHelper(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }
    
    public boolean isOnboardingCompleted() {
        return preferences.getBoolean(KEY_ONBOARDING_COMPLETED, false);
    }
    
    public void setOnboardingCompleted(boolean completed) {
        preferences.edit().putBoolean(KEY_ONBOARDING_COMPLETED, completed).apply();
    }
    
    public String getSelectedGoal() {
        return preferences.getString(KEY_SELECTED_GOAL, "");
    }
    
    public void setSelectedGoal(String goal) {
        preferences.edit().putString(KEY_SELECTED_GOAL, goal).apply();
    }
    
    public boolean isNotificationEnabled() {
        return preferences.getBoolean(KEY_NOTIFICATION_ENABLED, false);
    }
    
    public void setNotificationEnabled(boolean enabled) {
        preferences.edit().putBoolean(KEY_NOTIFICATION_ENABLED, enabled).apply();
    }
    
    public String getNotificationTime() {
        return preferences.getString(KEY_NOTIFICATION_TIME, "09:00");
    }
    
    public void setNotificationTime(String time) {
        preferences.edit().putString(KEY_NOTIFICATION_TIME, time).apply();
    }
    
    public int getDailyGoal() {
        return preferences.getInt(KEY_DAILY_GOAL, 10);
    }
    
    public void setDailyGoal(int goal) {
        preferences.edit().putInt(KEY_DAILY_GOAL, goal).apply();
    }
    
    public void clearAll() {
        preferences.edit().clear().apply();
    }
}

