package com.example.learningapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.learningapp.R;
import com.example.learningapp.activities.MainActivity;

public class HomeFragment extends Fragment {
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        view.findViewById(R.id.cardFlashcard).setOnClickListener(v ->
            navigateFromViewPager(R.id.action_homeFragment_to_flashcardHubFragment, null));
        
        view.findViewById(R.id.cardViewExams).setOnClickListener(v ->
            navigateFromViewPager(R.id.action_homeFragment_to_examListFragment, null));
        
        view.findViewById(R.id.cardPracticeTest).setOnClickListener(v ->
            navigateFromViewPager(R.id.action_homeFragment_to_practiceTestConfigFragment, null));
        
        view.findViewById(R.id.cardHistory).setOnClickListener(v ->
            navigateFromViewPager(R.id.action_homeFragment_to_historyFragment, null));
    }
    
    private void navigateFromViewPager(int actionId, Bundle args) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).navigateFromViewPager(actionId, args);
        }
    }
}

