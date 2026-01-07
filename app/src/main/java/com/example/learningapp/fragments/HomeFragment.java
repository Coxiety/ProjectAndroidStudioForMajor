package com.example.learningapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.learningapp.R;

public class HomeFragment extends Fragment {
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        NavController navController = Navigation.findNavController(view);
        
        view.findViewById(R.id.cardFlashcard).setOnClickListener(v ->
            navController.navigate(R.id.action_homeFragment_to_flashcardHubFragment));
        
        view.findViewById(R.id.cardPomodoro).setOnClickListener(v ->
            navController.navigate(R.id.action_homeFragment_to_pomodoroHubFragment));
        
        view.findViewById(R.id.cardViewExams).setOnClickListener(v ->
            navController.navigate(R.id.action_homeFragment_to_examListFragment));
        
        view.findViewById(R.id.cardPracticeTest).setOnClickListener(v ->
            navController.navigate(R.id.action_homeFragment_to_practiceTestConfigFragment));
        
        view.findViewById(R.id.cardHistory).setOnClickListener(v ->
            navController.navigate(R.id.action_homeFragment_to_historyFragment));
        
        view.findViewById(R.id.cardSettings).setOnClickListener(v ->
            navController.navigate(R.id.action_homeFragment_to_settingsFragment));
    }
}

