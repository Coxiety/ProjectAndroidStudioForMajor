package com.example.learningapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.learningapp.R;

public class PomodoroHubFragment extends Fragment {
    
    private Button btnStartPreset, btnStartCustom;
    private EditText etWorkDuration, etShortBreak, etLongBreak, etCycles;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_pomodoro_hub, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        btnStartPreset = view.findViewById(R.id.btnStartPreset);
        btnStartCustom = view.findViewById(R.id.btnStartCustom);
        etWorkDuration = view.findViewById(R.id.etWorkDuration);
        etShortBreak = view.findViewById(R.id.etShortBreak);
        etLongBreak = view.findViewById(R.id.etLongBreak);
        etCycles = view.findViewById(R.id.etCycles);
        
        btnStartPreset.setOnClickListener(v -> startPomodoroSession(25, 5, 15, 4));
        
        btnStartCustom.setOnClickListener(v -> {
            try {
                int workDuration = Integer.parseInt(etWorkDuration.getText().toString());
                int shortBreak = Integer.parseInt(etShortBreak.getText().toString());
                int longBreak = Integer.parseInt(etLongBreak.getText().toString());
                int cycles = Integer.parseInt(etCycles.getText().toString());
                
                if (workDuration <= 0 || shortBreak <= 0 || longBreak <= 0 || cycles <= 0) {
                    Toast.makeText(requireContext(), "Vui lòng nhập giá trị lớn hơn 0", Toast.LENGTH_SHORT).show();
                    return;
                }
                
                startPomodoroSession(workDuration, shortBreak, longBreak, cycles);
            } catch (NumberFormatException e) {
                Toast.makeText(requireContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void startPomodoroSession(int workDuration, int shortBreak, int longBreak, int cycles) {
        Bundle args = new Bundle();
        args.putInt("work_duration", workDuration);
        args.putInt("short_break", shortBreak);
        args.putInt("long_break", longBreak);
        args.putInt("cycles", cycles);
        
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_pomodoroHubFragment_to_pomodoroSessionFragment, args);
    }
}

