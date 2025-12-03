package com.example.learningapp.fragments.thithu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.learningapp.R;
import com.example.learningapp.database.DatabaseHelper;
import com.example.learningapp.models.ExamSet;

import java.util.List;

public class PracticeTestConfigFragment extends Fragment {
    
    private Button btnStartTest;
    private DatabaseHelper databaseHelper;
    private int examSetId = -1;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_practice_test_config, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        btnStartTest = view.findViewById(R.id.btnStartTest);
        databaseHelper = new DatabaseHelper(requireContext());
        
        // Get first exam set
        List<ExamSet> examSets = databaseHelper.getAllExamSets();
        if (!examSets.isEmpty()) {
            examSetId = examSets.get(0).getId();
        }
        
        btnStartTest.setOnClickListener(v -> startTest());
    }
    
    private void startTest() {
        if (examSetId == -1) {
            Toast.makeText(requireContext(), "Không tìm thấy bộ đề", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Navigate to PracticeTestFragment with arguments
        Bundle args = new Bundle();
        args.putInt("exam_set_id", examSetId);
        args.putInt("num_questions", 25);
        args.putInt("duration", 19);
        
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_practiceTestConfigFragment_to_practiceTestFragment, args);
    }
}

