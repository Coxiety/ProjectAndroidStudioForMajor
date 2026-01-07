package com.example.learningapp.utils;

public class StringUtils {
    
    /**
     * Check if a string is null, empty, or equals "null"
     * Useful for checking database/JSON values
     */
    public static boolean isNullOrEmpty(String text) {
        return text == null || text.trim().isEmpty() || text.equalsIgnoreCase("null");
    }
    
    /**
     * Check if an option (A, B, C, D) is valid for display
     */
    public static boolean isValidOption(String option) {
        return !isNullOrEmpty(option);
    }
}
