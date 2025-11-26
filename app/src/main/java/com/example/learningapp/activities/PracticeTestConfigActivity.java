package com.example.learningapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learningapp.R;
import com.example.learningapp.database.DatabaseHelper;
import com.example.learningapp.models.ExamSet;

import java.util.List;

public class PracticeTestConfigActivity extends AppCompatActivity {
    
    private Button btnStartTest;
    private DatabaseHelper databaseHelper;
    private int examSetId = -1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_test_config);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        btnStartTest = findViewById(R.id.btnStartTest);
        
        databaseHelper = new DatabaseHelper(this);
        
        // Get first exam set
        List<ExamSet> examSets = databaseHelper.getAllExamSets();
        if (!examSets.isEmpty()) {
            examSetId = examSets.get(0).getId();
        }
        
        btnStartTest.setOnClickListener(v -> startTest());
    }
    
    private void startTest() {
        if (examSetId == -1) {
            Toast.makeText(this, "Không tìm thấy bộ đề", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Fixed: 25 questions, 19 minutes
        Intent intent = new Intent(this, PracticeTestActivity.class);
        intent.putExtra("exam_set_id", examSetId);
        intent.putExtra("num_questions", 25);
        intent.putExtra("duration", 19);
        startActivity(intent);
        finish();
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

