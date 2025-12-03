package com.example.learningapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningapp.R;
import com.example.learningapp.models.Question;
import com.example.learningapp.utils.ImageHelper;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    
    private List<Question> questions;
    private Context context;
    private NavController navController;
    
    public QuestionAdapter(List<Question> questions, Context context, NavController navController) {
        this.questions = questions;
        this.context = context;
        this.navController = navController;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(questions.get(position), position + 1);
    }
    
    @Override
    public int getItemCount() {
        return questions.size();
    }
    
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionNumber, tvQuestion, tvOptionA, tvOptionB, tvOptionC, tvOptionD, tvCorrectAnswer, tvLietBadge;
        ImageView ivQuestionImage;
        CardView cardView;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            ivQuestionImage = itemView.findViewById(R.id.ivQuestionImage);
            tvOptionA = itemView.findViewById(R.id.tvOptionA);
            tvOptionB = itemView.findViewById(R.id.tvOptionB);
            tvOptionC = itemView.findViewById(R.id.tvOptionC);
            tvOptionD = itemView.findViewById(R.id.tvOptionD);
            tvCorrectAnswer = itemView.findViewById(R.id.tvCorrectAnswer);
            tvLietBadge = itemView.findViewById(R.id.tvLietBadge);
            cardView = (CardView) itemView;
        }
        
        public void bind(Question question, int number) {
            tvQuestionNumber.setText("Câu " + number);
            tvQuestion.setText(question.getQuestionText());
            
            if (question.isLiet()) {
                tvLietBadge.setVisibility(View.VISIBLE);
            } else {
                tvLietBadge.setVisibility(View.GONE);
            }
            
            ImageHelper.loadQuestionImage(context, ivQuestionImage, question.getImagePath());
            
            tvOptionA.setText("A. " + question.getOptionA());
            tvOptionB.setText("B. " + question.getOptionB());
            
            if (question.getOptionC() != null && !question.getOptionC().isEmpty() && !question.getOptionC().equals("null")) {
                tvOptionC.setVisibility(View.VISIBLE);
                tvOptionC.setText("C. " + question.getOptionC());
            } else {
                tvOptionC.setVisibility(View.GONE);
            }
            
            if (question.getOptionD() != null && !question.getOptionD().isEmpty() && !question.getOptionD().equals("null")) {
                tvOptionD.setVisibility(View.VISIBLE);
                tvOptionD.setText("D. " + question.getOptionD());
            } else {
                tvOptionD.setVisibility(View.GONE);
            }
            
            tvOptionA.setBackgroundColor(Color.parseColor("#F5F5F5"));
            tvOptionB.setBackgroundColor(Color.parseColor("#F5F5F5"));
            tvOptionC.setBackgroundColor(Color.parseColor("#F5F5F5"));
            tvOptionD.setBackgroundColor(Color.parseColor("#F5F5F5"));
            
            switch (question.getCorrectAnswer()) {
                case "A": tvOptionA.setBackgroundColor(Color.parseColor("#C8E6C9")); break;
                case "B": tvOptionB.setBackgroundColor(Color.parseColor("#C8E6C9")); break;
                case "C": tvOptionC.setBackgroundColor(Color.parseColor("#C8E6C9")); break;
                case "D": tvOptionD.setBackgroundColor(Color.parseColor("#C8E6C9")); break;
            }
            
            tvCorrectAnswer.setText("Đáp án đúng: " + question.getCorrectAnswer());
            
            cardView.setOnClickListener(v -> {
                Bundle args = new Bundle();
                args.putString("question_text", question.getQuestionText());
                args.putString("option_a", question.getOptionA());
                args.putString("option_b", question.getOptionB());
                args.putString("option_c", question.getOptionC());
                args.putString("option_d", question.getOptionD());
                args.putString("correct_answer", question.getCorrectAnswer());
                args.putString("image_path", question.getImagePath());
                args.putBoolean("is_liet", question.isLiet());
                navController.navigate(R.id.action_examDetailFragment_to_questionDetailFragment, args);
            });
        }
    }
}

