package com.example.learningapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learningapp.R;
import com.example.learningapp.database.DatabaseHelper;
import com.example.learningapp.models.ExamHistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TestResultActivity extends AppCompatActivity {
    
    private TextView tvScore, tvResult, tvCorrectCount, tvWrongCount;
    private Button btnReviewMistakes, btnViewAllAnswers, btnFinish;
    
    private DatabaseHelper databaseHelper;
    private int correctAnswers = 0;
    private int wrongAnswers = 0;
    private int totalQuestions;
    private int examSetId;
    private String examName;
    
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
        
        databaseHelper = new DatabaseHelper(this);
        examSetId = getIntent().getIntExtra("exam_set_id", -1);
        examName = "Đề thi thử";
        
        calculateResults();
        displayResults();
        saveToHistory();
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
    
    private void saveToHistory() {
        try {
            ExamHistory history = new ExamHistory();
            history.setExamSetId(examSetId);
            history.setExamName(examName + " (" + totalQuestions + " câu)");
            history.setTotalQuestions(totalQuestions);
            history.setCorrectAnswers(correctAnswers);
            history.setWrongAnswers(wrongAnswers);
            
            int score = (correctAnswers * 100) / totalQuestions;
            history.setScore(score);
            history.setPassed(score >= 80);
            history.setTestDate(System.currentTimeMillis());
            
            int durationMinutes = getIntent().getIntExtra("duration", 0);
            history.setDurationMinutes(durationMinutes);
            
            ArrayList<Integer> questionIds = getIntent().getIntegerArrayListExtra("question_ids");
            ArrayList<String> selectedAnswers = getIntent().getStringArrayListExtra("selected_answers");
            ArrayList<String> correctAnswersList = getIntent().getStringArrayListExtra("correct_answers");
            
            JSONArray answersArray = new JSONArray();
            if (questionIds != null && selectedAnswers != null && correctAnswersList != null) {
                for (int i = 0; i < questionIds.size(); i++) {
                    JSONObject answerObj = new JSONObject();
                    answerObj.put("question_id", questionIds.get(i));
                    answerObj.put("selected", selectedAnswers.get(i));
                    answerObj.put("correct", correctAnswersList.get(i));
                    answersArray.put(answerObj);
                }
            }
            history.setAnswersJson(answersArray.toString());
            
            databaseHelper.saveExamHistory(history);
        } catch (JSONException e) {
            e.printStackTrace();
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

