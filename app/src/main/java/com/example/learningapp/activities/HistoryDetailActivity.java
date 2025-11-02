package com.example.learningapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learningapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HistoryDetailActivity extends AppCompatActivity {
    
    private TextView tvExamName, tvScore, tvTestDate, tvCorrectAnswers, tvWrongAnswers, tvDuration;
    private Button btnReviewMistakes;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        tvExamName = findViewById(R.id.tvExamName);
        tvScore = findViewById(R.id.tvScore);
        tvTestDate = findViewById(R.id.tvTestDate);
        tvCorrectAnswers = findViewById(R.id.tvCorrectAnswers);
        tvWrongAnswers = findViewById(R.id.tvWrongAnswers);
        tvDuration = findViewById(R.id.tvDuration);
        btnReviewMistakes = findViewById(R.id.btnReviewMistakes);
        
        displayHistoryDetails();
        
        btnReviewMistakes.setOnClickListener(v -> {
            // Could implement review functionality here
            finish();
        });
    }
    
    private void displayHistoryDetails() {
        String examName = getIntent().getStringExtra("exam_name");
        int correctAnswers = getIntent().getIntExtra("correct_answers", 0);
        int wrongAnswers = getIntent().getIntExtra("wrong_answers", 0);
        int totalQuestions = getIntent().getIntExtra("total_questions", 0);
        long testDate = getIntent().getLongExtra("test_date", 0);
        int duration = getIntent().getIntExtra("duration", 0);
        
        tvExamName.setText(examName);
        tvScore.setText(correctAnswers + "/" + totalQuestions);
        tvCorrectAnswers.setText(String.valueOf(correctAnswers));
        tvWrongAnswers.setText(String.valueOf(wrongAnswers));
        tvDuration.setText(duration + " phút");
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        tvTestDate.setText("Ngày: " + dateFormat.format(new Date(testDate)));
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

