package com.example.learningapp.fragments.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.learningapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HistoryDetailFragment extends Fragment {
    
    private TextView tvExamName, tvScore, tvTestDate, tvCorrectAnswers, tvWrongAnswers, tvDuration;
    private Button btnReviewMistakes;
    
    private int examSetId;
    private String answersJson;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history_detail, container, false);
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
        
        btnReviewMistakes.setOnClickListener(v -> navigateToReviewMistakes(view));
    }
    
    private void displayHistoryDetails() {
        Bundle args = getArguments();
        if (args != null) {
            examSetId = args.getInt("exam_set_id", -1);
            answersJson = args.getString("answers_json", null);
            
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
            tvDuration.setText(duration + " phut");
            
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            tvTestDate.setText("Ngay: " + dateFormat.format(new Date(testDate)));
            
            // Disable button if no answers data
            if (answersJson == null || answersJson.isEmpty()) {
                btnReviewMistakes.setEnabled(false);
                btnReviewMistakes.setAlpha(0.5f);
            }
        }
    }
    
    private void navigateToReviewMistakes(View view) {
        if (answersJson == null || answersJson.isEmpty()) {
            Toast.makeText(requireContext(), "Khong co du lieu de on lai", Toast.LENGTH_SHORT).show();
            return;
        }
        
        try {
            JSONArray answersArray = new JSONArray(answersJson);
            
            ArrayList<Integer> questionIds = new ArrayList<>();
            ArrayList<String> selectedAnswers = new ArrayList<>();
            ArrayList<String> correctAnswers = new ArrayList<>();
            
            for (int i = 0; i < answersArray.length(); i++) {
                JSONObject answerObj = answersArray.getJSONObject(i);
                questionIds.add(answerObj.getInt("question_id"));
                selectedAnswers.add(answerObj.optString("selected", ""));
                correctAnswers.add(answerObj.optString("correct", ""));
            }
            
            Bundle args = new Bundle();
            args.putInt("exam_set_id", examSetId);
            args.putIntegerArrayList("question_ids", questionIds);
            args.putStringArrayList("selected_answers", selectedAnswers);
            args.putStringArrayList("correct_answers", correctAnswers);
            args.putString("filter_mode", "wrong");
            
            Navigation.findNavController(view)
                    .navigate(R.id.action_historyDetailFragment_to_reviewMistakesFragment, args);
            
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(requireContext(), "Loi doc du lieu", Toast.LENGTH_SHORT).show();
        }
    }
}
