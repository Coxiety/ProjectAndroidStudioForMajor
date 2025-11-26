package com.example.learningapp.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.learningapp.R;
import com.example.learningapp.utils.ImageHelper;

public class QuestionDetailActivity extends AppCompatActivity {
    
    private TextView tvQuestion, tvOptionA, tvOptionB, tvOptionC, tvOptionD, tvLietBadge;
    private ImageView ivQuestionImage;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_detail);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Chi tiết câu hỏi");
        }
        
        tvQuestion = findViewById(R.id.tvQuestion);
        tvOptionA = findViewById(R.id.tvOptionA);
        tvOptionB = findViewById(R.id.tvOptionB);
        tvOptionC = findViewById(R.id.tvOptionC);
        tvOptionD = findViewById(R.id.tvOptionD);
        tvLietBadge = findViewById(R.id.tvLietBadge);
        ivQuestionImage = findViewById(R.id.ivQuestionImage);
        
        String questionText = getIntent().getStringExtra("question_text");
        String optionA = getIntent().getStringExtra("option_a");
        String optionB = getIntent().getStringExtra("option_b");
        String optionC = getIntent().getStringExtra("option_c");
        String optionD = getIntent().getStringExtra("option_d");
        String correctAnswer = getIntent().getStringExtra("correct_answer");
        String imagePath = getIntent().getStringExtra("image_path");
        boolean isLiet = getIntent().getBooleanExtra("is_liet", false);
        
        tvQuestion.setText(questionText);
        
        if (isLiet) {
            tvLietBadge.setVisibility(View.VISIBLE);
        } else {
            tvLietBadge.setVisibility(View.GONE);
        }
        
        setOptionText(tvOptionA, "A", optionA);
        setOptionText(tvOptionB, "B", optionB);
        setOptionText(tvOptionC, "C", optionC);
        setOptionText(tvOptionD, "D", optionD);
        
        highlightCorrectAnswer(correctAnswer);
        
        ImageHelper.loadQuestionImage(this, ivQuestionImage, imagePath);
    }
    
    private void setOptionText(TextView textView, String label, String text) {
        if (isNullOrEmpty(text)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(label + ". " + text);
        }
    }
    
    private boolean isNullOrEmpty(String text) {
        return text == null || text.trim().isEmpty() || text.equalsIgnoreCase("null");
    }
    
    private void highlightCorrectAnswer(String correctAnswer) {
        if (isNullOrEmpty(correctAnswer)) {
            return;
        }
        
        int correctColor = ContextCompat.getColor(this, R.color.success);
        
        TextView correctTextView = null;
        switch (correctAnswer) {
            case "A":
                correctTextView = tvOptionA;
                break;
            case "B":
                correctTextView = tvOptionB;
                break;
            case "C":
                correctTextView = tvOptionC;
                break;
            case "D":
                correctTextView = tvOptionD;
                break;
        }
        
        if (correctTextView != null && correctTextView.getVisibility() == View.VISIBLE) {
            correctTextView.setTextColor(correctColor);
            correctTextView.setTypeface(null, Typeface.BOLD);
            correctTextView.setBackgroundColor(ContextCompat.getColor(this, R.color.success_light));
        }
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

