package com.example.learningapp.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.learningapp.R;

public class MainActivity extends AppCompatActivity {
    
    private CardView cardFlashcard, cardPomodoro, cardViewExams, 
                     cardPracticeTest, cardHistory, cardSettings;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        cardFlashcard = findViewById(R.id.cardFlashcard);
        cardPomodoro = findViewById(R.id.cardPomodoro);
        cardViewExams = findViewById(R.id.cardViewExams);
        cardPracticeTest = findViewById(R.id.cardPracticeTest);
        cardHistory = findViewById(R.id.cardHistory);
        cardSettings = findViewById(R.id.cardSettings);
        
        cardFlashcard.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FlashcardHubActivity.class);
            startActivity(intent);
        });
        
        cardPomodoro.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PomodoroHubActivity.class);
            startActivity(intent);
        });
        
        cardViewExams.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ExamListActivity.class);
            startActivity(intent);
        });
        
        cardPracticeTest.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PracticeTestConfigActivity.class);
            startActivity(intent);
        });
        
        cardHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(intent);
        });
        
        cardSettings.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });
    }
}

