package com.example.learningapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.SwitchCompat;

import com.example.learningapp.R;

public class SettingsFragment extends Fragment {
    
    private SwitchCompat switchNotifications;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        switchNotifications = view.findViewById(R.id.switchNotifications);
        
        // TODO: Load saved preferences
        // TODO: Setup switch listeners to save preferences
        
        if (switchNotifications != null) {
            switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
                // TODO: Toggle notifications
            });
        }
    }
}
