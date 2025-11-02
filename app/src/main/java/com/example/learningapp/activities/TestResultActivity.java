package com.example.learningapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learningapp.R;

import java.util.ArrayList;

public class TestResultActivity extends AppCompatActivity {
    
    private TextView tvScore, tvResult, tvCorrectCount, tvWrongCount;
    private Button btnReviewMistakes, btnViewAllAnswers, btnFinish;
    
    private int correctAnswers = 0;
    private int wrongAnswers = 0;
    private int totalQuestions;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_result);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
        
        tvScore = findViewById(R.id.tvScore);
        tvResult = findViewById(R.id.tvResult);
        tvCorrectCount = findViewById(R.id.tvCorrectCount);
        tvWrongCount = findViewById(R.id.tvWrongCount);
        btnReviewMistakes = findViewById(R.id.btnReviewMistakes);
        btnViewAllAnswers = findViewById(R.id.btnViewAllAnswers);
        btnFinish = findViewById(R.id.btnFinish);
        
        calculateResults();
        displayResults();
        setupListeners();
    }
    
    private void calculateResults() {
        totalQuestions = getIntent().getIntExtra("total_questions", 0);
        ArrayList<String> selectedAnswers = getIntent().getStringArrayListExtra("selected_answers");
        ArrayList<String> correctAnswersList = getIntent().getStringArrayListExtra("correct_answers");
        
        if (selectedAnswers != null && correctAnswersList != null) {
            for (int i = 0; i < selectedAnswers.size(); i++) {
                String selected = selectedAnswers.get(i);
                String correct = correctAnswersList.get(i);
                
                if (selected != null && selected.equals(correct)) {
                    correctAnswers++;
                } else {
                    wrongAnswers++;
                }
            }
        }
    }
    
    private void displayResults() {
        tvScore.setText(correctAnswers + "/" + totalQuestions);
        tvCorrectCount.setText(String.valueOf(correctAnswers));
        tvWrongCount.setText(String.valueOf(wrongAnswers));
        
        double percentage = (correctAnswers * 100.0) / totalQuestions;
        if (percentage >= 80) {
            tvResult.setText(R.string.pass);
            tvResult.setTextColor(getResources().getColor(R.color.success, null));
        } else {
            tvResult.setText(R.string.fail);
            tvResult.setTextColor(getResources().getColor(R.color.error, null));
        }
    }
    
    private void setupListeners() {
        btnReviewMistakes.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReviewMistakesActivity.class);
            intent.putExtras(getIntent());
            intent.putExtra("filter_mode", "wrong");
            startActivity(intent);
        });
        
        btnViewAllAnswers.setOnClickListener(v -> {
            Intent intent = new Intent(this, ReviewMistakesActivity.class);
            intent.putExtras(getIntent());
            intent.putExtra("filter_mode", "all");
            startActivity(intent);
        });
        
        btnFinish.setOnClickListener(v -> {
            finish();
        });
    }
    
    @Override
    public void onBackPressed() {
        finish();
    }
}

