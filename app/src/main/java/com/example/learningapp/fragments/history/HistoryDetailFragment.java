package com.example.learningapp.fragments.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.learningapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HistoryDetailFragment extends Fragment {
    
    private TextView tvExamName, tvScore, tvTestDate, tvCorrectAnswers, tvWrongAnswers, tvDuration;
    private Button btnReviewMistakes;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_history_detail, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        tvExamName = view.findViewById(R.id.tvExamName);
        tvScore = view.findViewById(R.id.tvScore);
        tvTestDate = view.findViewById(R.id.tvTestDate);
        tvCorrectAnswers = view.findViewById(R.id.tvCorrectAnswers);
        tvWrongAnswers = view.findViewById(R.id.tvWrongAnswers);
        tvDuration = view.findViewById(R.id.tvDuration);
        btnReviewMistakes = view.findViewById(R.id.btnReviewMistakes);
        
        displayHistoryDetails();
        
        btnReviewMistakes.setOnClickListener(v -> requireActivity().onBackPressed());
    }
    
    private void displayHistoryDetails() {
        Bundle args = getArguments();
        if (args != null) {
            String examName = args.getString("exam_name");
            int correctAnswers = args.getInt("correct_answers", 0);
            int wrongAnswers = args.getInt("wrong_answers", 0);
            int totalQuestions = args.getInt("total_questions", 0);
            long testDate = args.getLong("test_date", 0);
            int duration = args.getInt("duration", 0);
            
            tvExamName.setText(examName);
            tvScore.setText(correctAnswers + "/" + totalQuestions);
            tvCorrectAnswers.setText(String.valueOf(correctAnswers));
            tvWrongAnswers.setText(String.valueOf(wrongAnswers));
            tvDuration.setText(duration + " phút");
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            tvTestDate.setText("Ngày: " + dateFormat.format(new Date(testDate)));
        }
    }
}

