package com.example.learningapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learningapp.R;
import com.example.learningapp.database.DatabaseHelper;
import com.example.learningapp.models.ExamSet;

import java.util.ArrayList;
import java.util.List;

public class PracticeTestConfigActivity extends AppCompatActivity {
    
    private Spinner spinnerExamSet;
    private EditText etNumQuestions, etDuration;
    private Button btnStartTest;
    private DatabaseHelper databaseHelper;
    private List<ExamSet> examSets;
    private int selectedExamSetId = -1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_test_config);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        spinnerExamSet = findViewById(R.id.spinnerExamSet);
        etNumQuestions = findViewById(R.id.etNumQuestions);
        etDuration = findViewById(R.id.etDuration);
        btnStartTest = findViewById(R.id.btnStartTest);
        
        databaseHelper = new DatabaseHelper(this);
        loadExamSets();
        
        btnStartTest.setOnClickListener(v -> startTest());
    }
    
    private void loadExamSets() {
        examSets = databaseHelper.getAllExamSets();
        List<String> examNames = new ArrayList<>();
        for (ExamSet examSet : examSets) {
            examNames.add(examSet.getName());
        }
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, examNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerExamSet.setAdapter(adapter);
        
        spinnerExamSet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedExamSetId = examSets.get(position).getId();
                etNumQuestions.setText(String.valueOf(examSets.get(position).getQuestionCount()));
            }
            
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    
    private void startTest() {
        if (selectedExamSetId == -1) {
            Toast.makeText(this, "Vui lòng chọn bộ đề", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            int numQuestions = Integer.parseInt(etNumQuestions.getText().toString());
            int duration = Integer.parseInt(etDuration.getText().toString());
            
            if (numQuestions <= 0 || duration <= 0) {
                Toast.makeText(this, "Vui lòng nhập giá trị hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }
            
            Intent intent = new Intent(this, PracticeTestActivity.class);
            intent.putExtra("exam_set_id", selectedExamSetId);
            intent.putExtra("num_questions", numQuestions);
            intent.putExtra("duration", duration);
            startActivity(intent);
            finish();
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        }
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

