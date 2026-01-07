package com.example.learningapp.fragments.thithu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.learningapp.R;
import com.example.learningapp.database.DatabaseHelper;
import com.example.learningapp.models.ExamHistory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TestResultFragment extends Fragment {
    
    private TextView tvScore, tvResult, tvCorrectCount, tvWrongCount, tvFailReason;
    private Button btnReviewMistakes, btnViewAllAnswers, btnFinish;
    
    private DatabaseHelper databaseHelper;
    private int correctAnswers = 0;
    private int wrongAnswers = 0;
    private int totalQuestions;
    private int examSetId;
    private String examName;
    private boolean failedDueToLiet = false;
    private boolean hasCalculated = false;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test_result, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        if (requireActivity() instanceof androidx.appcompat.app.AppCompatActivity) {
            androidx.appcompat.app.AppCompatActivity activity = (androidx.appcompat.app.AppCompatActivity) requireActivity();
            if (activity.getSupportActionBar() != null) {
                activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            }
        }
        
        tvScore = view.findViewById(R.id.tvScore);
        tvResult = view.findViewById(R.id.tvResult);
        tvCorrectCount = view.findViewById(R.id.tvCorrectCount);
        tvWrongCount = view.findViewById(R.id.tvWrongCount);
        tvFailReason = view.findViewById(R.id.tvFailReason);
        btnReviewMistakes = view.findViewById(R.id.btnReviewMistakes);
        btnViewAllAnswers = view.findViewById(R.id.btnViewAllAnswers);
        btnFinish = view.findViewById(R.id.btnFinish);
        
        databaseHelper = new DatabaseHelper(requireContext());
        
        Bundle args = getArguments();
        if (args != null) {
            examSetId = args.getInt("exam_set_id", -1);
            examName = "Đề thi thử";
            
            if (savedInstanceState != null) {
                correctAnswers = savedInstanceState.getInt("correct_answers", 0);
                wrongAnswers = savedInstanceState.getInt("wrong_answers", 0);
                totalQuestions = savedInstanceState.getInt("total_questions", 0);
                failedDueToLiet = savedInstanceState.getBoolean("failed_due_to_liet", false);
                displayResults();
            } else if (!hasCalculated) {
                calculateResults();
                displayResults();
                saveToHistory();
                hasCalculated = true;
            } else {
                displayResults();
            }
            setupListeners();
            disableBackButton();
        }
    }
    
    private void disableBackButton() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                NavController navController = Navigation.findNavController(requireView());
                navController.popBackStack(R.id.homeFragment, false);
            }
        });
    }
    
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("correct_answers", correctAnswers);
        outState.putInt("wrong_answers", wrongAnswers);
        outState.putInt("total_questions", totalQuestions);
        outState.putBoolean("failed_due_to_liet", failedDueToLiet);
    }
    
    private void calculateResults() {
        Bundle args = getArguments();
        if (args == null) return;
        
        correctAnswers = 0;
        wrongAnswers = 0;
        failedDueToLiet = false;
        
        totalQuestions = args.getInt("total_questions", 0);
        ArrayList<String> selectedAnswers = args.getStringArrayList("selected_answers");
        ArrayList<String> correctAnswersList = args.getStringArrayList("correct_answers");
        ArrayList<Boolean> isLietList = (ArrayList<Boolean>) args.getSerializable("is_liet_list");
        
        if (selectedAnswers != null && correctAnswersList != null) {
            for (int i = 0; i < selectedAnswers.size(); i++) {
                String selected = selectedAnswers.get(i);
                String correct = correctAnswersList.get(i);
                boolean isLiet = isLietList != null && i < isLietList.size() && isLietList.get(i);
                
                if (selected != null && selected.equals(correct)) {
                    correctAnswers++;
                } else {
                    wrongAnswers++;
                    if (isLiet) {
                        failedDueToLiet = true;
                    }
                }
            }
        }
    }
    
    private void displayResults() {
        tvScore.setText(correctAnswers + "/" + totalQuestions);
        tvCorrectCount.setText(String.valueOf(correctAnswers));
        tvWrongCount.setText(String.valueOf(wrongAnswers));
        
        boolean isPassed = wrongAnswers < 4 && !failedDueToLiet;
        
        if (isPassed) {
            tvResult.setText(R.string.pass);
            tvResult.setTextColor(requireContext().getResources().getColor(R.color.success, null));
            tvFailReason.setVisibility(View.GONE);
        } else {
            tvResult.setText(R.string.fail);
            tvResult.setTextColor(requireContext().getResources().getColor(R.color.error, null));
            
            if (failedDueToLiet) {
                tvFailReason.setText("⚠️ Lý do: Sai câu liệt");
                tvFailReason.setVisibility(View.VISIBLE);
            } else if (wrongAnswers >= 4) {
                tvFailReason.setText("⚠️ Lý do: Sai " + wrongAnswers + " câu (tối đa 3 câu)");
                tvFailReason.setVisibility(View.VISIBLE);
            } else {
                tvFailReason.setVisibility(View.GONE);
            }
        }
    }
    
    private void saveToHistory() {
        Bundle args = getArguments();
        if (args == null) return;
        
        try {
            ExamHistory history = new ExamHistory();
            history.setExamSetId(examSetId);
            history.setExamName(examName + " (" + totalQuestions + " câu)");
            history.setTotalQuestions(totalQuestions);
            history.setCorrectAnswers(correctAnswers);
            history.setWrongAnswers(wrongAnswers);
            
            int score = (correctAnswers * 100) / totalQuestions;
            history.setScore(score);
            boolean isPassed = wrongAnswers < 4 && !failedDueToLiet;
            history.setPassed(isPassed);
            history.setTestDate(System.currentTimeMillis());
            
            int durationMinutes = args.getInt("duration", 0);
            history.setDurationMinutes(durationMinutes);
            
            ArrayList<Integer> questionIds = args.getIntegerArrayList("question_ids");
            ArrayList<String> selectedAnswers = args.getStringArrayList("selected_answers");
            ArrayList<String> correctAnswersList = args.getStringArrayList("correct_answers");
            
            JSONArray answersArray = new JSONArray();
            if (questionIds != null && selectedAnswers != null && correctAnswersList != null) {
                for (int i = 0; i < questionIds.size(); i++) {
                    JSONObject answerObj = new JSONObject();
                    answerObj.put("question_id", questionIds.get(i));
                    answerObj.put("selected", selectedAnswers.get(i));
                    answerObj.put("correct", correctAnswersList.get(i));
                    answersArray.put(answerObj);
                }
            }
            history.setAnswersJson(answersArray.toString());
            
            databaseHelper.saveExamHistory(history);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    
    private void setupListeners() {
        btnReviewMistakes.setOnClickListener(v -> {
            Bundle args = getArguments();
            if (args != null) {
                Bundle reviewArgs = new Bundle(args);
                reviewArgs.putString("filter_mode", "wrong");
                
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_testResultFragment_to_reviewMistakesFragment, reviewArgs);
            }
        });
        
        btnViewAllAnswers.setOnClickListener(v -> {
            Bundle args = getArguments();
            if (args != null) {
                Bundle reviewArgs = new Bundle(args);
                reviewArgs.putString("filter_mode", "all");
                
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_testResultFragment_to_reviewMistakesFragment, reviewArgs);
            }
        });
        
        btnFinish.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);
            navController.popBackStack(R.id.homeFragment, false);
        });
    }
}

