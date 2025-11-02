package com.example.learningapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class ImageHelper {
    
    public static void loadQuestionImage(Context context, ImageView imageView, String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            try {
                String fullPath = "Images/" + imagePath;
                InputStream inputStream = context.getAssets().open(fullPath);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.GONE);
                }
                
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
                imageView.setVisibility(View.GONE);
            }
        } else {
            imageView.setVisibility(View.GONE);
        }
    }
    
    public static void loadFlashcardImage(Context context, ImageView imageView, String imagePath) {
        loadQuestionImage(context, imageView, imagePath);
    }
    
    public static boolean hasImage(String imagePath) {
        return imagePath != null && !imagePath.isEmpty();
    }
}

