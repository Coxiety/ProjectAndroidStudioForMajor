package com.example.learningapp.fragments.dethi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningapp.R;
import com.example.learningapp.adapters.QuestionAdapter;
import com.example.learningapp.database.DatabaseHelper;
import com.example.learningapp.models.Question;

import java.util.List;

public class ExamDetailFragment extends Fragment {
    
    private TextView tvExamName, tvQuestionCount;
    private RecyclerView recyclerViewQuestions;
    private DatabaseHelper databaseHelper;
    private List<Question> questions;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exam_detail, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        tvExamName = view.findViewById(R.id.tvExamName);
        tvQuestionCount = view.findViewById(R.id.tvQuestionCount);
        recyclerViewQuestions = view.findViewById(R.id.recyclerViewQuestions);
        recyclerViewQuestions.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        Bundle args = getArguments();
        if (args != null) {
            int examSetId = args.getInt("exam_set_id", -1);
            String examName = args.getString("exam_name");
            int questionCount = args.getInt("question_count", 0);
            
            tvExamName.setText(examName);
            tvQuestionCount.setText(questionCount + " câu hỏi");
            
            databaseHelper = new DatabaseHelper(requireContext());
            loadQuestions(examSetId);
        }
    }
    
    private void loadQuestions(int examSetId) {
        questions = databaseHelper.getQuestionsByExamSet(examSetId);
        NavController navController = Navigation.findNavController(requireView());
        QuestionAdapter adapter = new QuestionAdapter(questions, requireContext(), navController);
        recyclerViewQuestions.setAdapter(adapter);
    }
}

