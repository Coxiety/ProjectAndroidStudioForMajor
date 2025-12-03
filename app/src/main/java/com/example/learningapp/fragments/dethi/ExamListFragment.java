package com.example.learningapp.fragments.dethi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningapp.R;
import com.example.learningapp.adapters.ExamSetAdapter;
import com.example.learningapp.database.DatabaseHelper;
import com.example.learningapp.models.ExamSet;

import java.util.List;

public class ExamListFragment extends Fragment {
    
    private RecyclerView recyclerViewExams;
    private DatabaseHelper databaseHelper;
    private List<ExamSet> examSets;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_exam_list, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        recyclerViewExams = view.findViewById(R.id.recyclerViewExams);
        recyclerViewExams.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        databaseHelper = new DatabaseHelper(requireContext());
        loadExamSets();
    }
    
    private void loadExamSets() {
        examSets = databaseHelper.getAllExamSets();
        NavController navController = Navigation.findNavController(requireView());
        ExamSetAdapter adapter = new ExamSetAdapter(examSets, requireContext(), navController);
        recyclerViewExams.setAdapter(adapter);
    }
}

