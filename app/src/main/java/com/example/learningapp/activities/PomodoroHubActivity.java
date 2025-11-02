package com.example.learningapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.learningapp.R;
import com.example.learningapp.models.PomodoroSession;

public class PomodoroHubActivity extends AppCompatActivity {
    
    private Button btnStartPreset, btnStartCustom;
    private EditText etWorkDuration, etShortBreak, etLongBreak, etCycles;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomodoro_hub);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        btnStartPreset = findViewById(R.id.btnStartPreset);
        btnStartCustom = findViewById(R.id.btnStartCustom);
        etWorkDuration = findViewById(R.id.etWorkDuration);
        etShortBreak = findViewById(R.id.etShortBreak);
        etLongBreak = findViewById(R.id.etLongBreak);
        etCycles = findViewById(R.id.etCycles);
        
        btnStartPreset.setOnClickListener(v -> startPomodoroSession(25, 5, 15, 4));
        
        btnStartCustom.setOnClickListener(v -> {
            try {
                int workDuration = Integer.parseInt(etWorkDuration.getText().toString());
                int shortBreak = Integer.parseInt(etShortBreak.getText().toString());
                int longBreak = Integer.parseInt(etLongBreak.getText().toString());
                int cycles = Integer.parseInt(etCycles.getText().toString());
                
                if (workDuration <= 0 || shortBreak <= 0 || longBreak <= 0 || cycles <= 0) {
                    Toast.makeText(this, "Vui lòng nhập giá trị lớn hơn 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                startPomodoroSession(workDuration, shortBreak, longBreak, cycles);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void startPomodoroSession(int workDuration, int shortBreak, int longBreak, int cycles) {
        Intent intent = new Intent(this, PomodoroSessionActivity.class);
        intent.putExtra("work_duration", workDuration);
        intent.putExtra("short_break", shortBreak);
        intent.putExtra("long_break", longBreak);
        intent.putExtra("cycles", cycles);
        startActivity(intent);
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

