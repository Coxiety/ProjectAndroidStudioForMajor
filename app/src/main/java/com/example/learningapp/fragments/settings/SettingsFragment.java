package com.example.learningapp.fragments.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.learningapp.R;
import com.example.learningapp.utils.PreferencesHelper;

public class SettingsFragment extends Fragment {
    
    private SwitchCompat switchNotifications;
    private EditText etDailyGoal;
    private Button btnSaveGoal;
    private PreferencesHelper preferencesHelper;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_settings, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        switchNotifications = view.findViewById(R.id.switchNotifications);
        etDailyGoal = view.findViewById(R.id.etDailyGoal);
        btnSaveGoal = view.findViewById(R.id.btnSaveGoal);
        
        preferencesHelper = new PreferencesHelper(requireContext());
        
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
            Toast.makeText(requireContext(), isChecked ? "Bật thông báo" : "Tắt thông báo", 
                    Toast.LENGTH_SHORT).show();
        });
        
        btnSaveGoal.setOnClickListener(v -> {
            try {
                int goal = Integer.parseInt(etDailyGoal.getText().toString());
                if (goal > 0) {
                    preferencesHelper.setDailyGoal(goal);
                    Toast.makeText(requireContext(), R.string.success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext(), "Vui lòng nhập giá trị lớn hơn 0", 
                            Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Vui lòng nhập số hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

