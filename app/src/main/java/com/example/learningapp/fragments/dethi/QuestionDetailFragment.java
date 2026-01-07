package com.example.learningapp.fragments.dethi;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.learningapp.R;
import com.example.learningapp.utils.ImageHelper;
import com.example.learningapp.utils.StringUtils;

public class QuestionDetailFragment extends Fragment {
    
    private TextView tvQuestion, tvOptionA, tvOptionB, tvOptionC, tvOptionD, tvLietBadge;
    private ImageView ivQuestionImage;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_question_detail, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        tvQuestion = view.findViewById(R.id.tvQuestion);
        tvOptionA = view.findViewById(R.id.tvOptionA);
        tvOptionB = view.findViewById(R.id.tvOptionB);
        tvOptionC = view.findViewById(R.id.tvOptionC);
        tvOptionD = view.findViewById(R.id.tvOptionD);
        tvLietBadge = view.findViewById(R.id.tvLietBadge);
        ivQuestionImage = view.findViewById(R.id.ivQuestionImage);
        
        Bundle args = getArguments();
        if (args != null) {
            String questionText = args.getString("question_text");
            String optionA = args.getString("option_a");
            String optionB = args.getString("option_b");
            String optionC = args.getString("option_c");
            String optionD = args.getString("option_d");
            String correctAnswer = args.getString("correct_answer");
            String imagePath = args.getString("image_path");
            boolean isLiet = args.getBoolean("is_liet", false);
            
            tvQuestion.setText(questionText);
            
            if (isLiet) {
                tvLietBadge.setVisibility(View.VISIBLE);
            } else {
                tvLietBadge.setVisibility(View.GONE);
            }
            
            setOptionText(tvOptionA, "A", optionA);
            setOptionText(tvOptionB, "B", optionB);
            setOptionText(tvOptionC, "C", optionC);
            setOptionText(tvOptionD, "D", optionD);
            
            highlightCorrectAnswer(correctAnswer);
            
            ImageHelper.loadQuestionImage(requireContext(), ivQuestionImage, imagePath);
        }
    }
    
    private void setOptionText(TextView textView, String label, String text) {
        if (StringUtils.isNullOrEmpty(text)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(label + ". " + text);
        }
    }
    
    private void highlightCorrectAnswer(String correctAnswer) {
        if (StringUtils.isNullOrEmpty(correctAnswer)) {
            return;
        }
        
        int correctColor = ContextCompat.getColor(requireContext(), R.color.success);
        
        TextView correctTextView = null;
        switch (correctAnswer) {
            case "A": correctTextView = tvOptionA; break;
            case "B": correctTextView = tvOptionB; break;
            case "C": correctTextView = tvOptionC; break;
            case "D": correctTextView = tvOptionD; break;
        }
        
        if (correctTextView != null && correctTextView.getVisibility() == View.VISIBLE) {
            correctTextView.setTextColor(correctColor);
            correctTextView.setTypeface(null, Typeface.BOLD);
            correctTextView.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.success_light));
        }
    }
}

