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
        if (imagePath != null && !imagePath.isEmpty() && !imagePath.equals("null")) {
            try {
                String fullPath = "A1_250Q_images/" + imagePath;
                android.util.Log.d("ImageHelper", "Loading image from: " + fullPath);
                InputStream inputStream = context.getAssets().open(fullPath);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    imageView.setVisibility(View.VISIBLE);
                    android.util.Log.d("ImageHelper", "Image loaded successfully: " + fullPath);
                } else {
                    imageView.setVisibility(View.GONE);
                    android.util.Log.w("ImageHelper", "Bitmap is null for: " + fullPath);
                }
                
                inputStream.close();
            } catch (IOException e) {
                android.util.Log.e("ImageHelper", "Error loading image: " + imagePath + " - " + e.getMessage());
                e.printStackTrace();
                imageView.setVisibility(View.GONE);
            }
        } else {
            imageView.setVisibility(View.GONE);
        }
    }
    
    public static boolean hasImage(String imagePath) {
        return imagePath != null && !imagePath.isEmpty();
    }
}

