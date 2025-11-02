package com.example.learningapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.example.learningapp.R;
import com.example.learningapp.utils.PreferencesHelper;

public class SettingsActivity extends AppCompatActivity {
    
    private SwitchCompat switchNotifications;
    private EditText etDailyGoal;
    private Button btnSaveGoal, btnBackup, btnRestore;
    private PreferencesHelper preferencesHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        switchNotifications = findViewById(R.id.switchNotifications);
        etDailyGoal = findViewById(R.id.etDailyGoal);
        btnSaveGoal = findViewById(R.id.btnSaveGoal);
        btnBackup = findViewById(R.id.btnBackup);
        btnRestore = findViewById(R.id.btnRestore);
        
        preferencesHelper = new PreferencesHelper(this);
        
        loadSettings();
        setupListeners();
    }
    
    private void loadSettings() {
        switchNotifications.setChecked(preferencesHelper.isNotificationEnabled());
        etDailyGoal.setText(String.valueOf(preferencesHelper.getDailyGoal()));
    }
    
    private void setupListeners() {
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            preferencesHelper.setNotificationEnabled(isChecked);
            Toast.makeText(this, isChecked ? "Bật thông báo" : "Tắt thông báo", 
                    Toast.LENGTH_SHORT).show();
        });
        
        btnSaveGoal.setOnClickListener(v -> {
            try {
                int goal = Integer.parseInt(etDailyGoal.getText().toString());
                if (goal > 0) {
                    preferencesHelper.setDailyGoal(goal);
                    Toast.makeText(this, R.string.success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Vui lòng nhập giá trị lớn hơn 0", 
                            Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Vui lòng nhập số hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });
        
        btnBackup.setOnClickListener(v -> {
            Toast.makeText(this, "Tính năng backup sẽ được cập nhật", 
                    Toast.LENGTH_SHORT).show();
        });
        
        btnRestore.setOnClickListener(v -> {
            Toast.makeText(this, "Tính năng restore sẽ được cập nhật", 
                    Toast.LENGTH_SHORT).show();
        });
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

