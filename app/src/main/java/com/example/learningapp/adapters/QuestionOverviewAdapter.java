package com.example.learningapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningapp.R;
import com.example.learningapp.models.Question;
import com.example.learningapp.models.UserAnswer;

import java.util.ArrayList;
import java.util.List;

public class QuestionOverviewAdapter extends RecyclerView.Adapter<QuestionOverviewAdapter.ViewHolder> {
    
    private List<Question> questions;
    private List<UserAnswer> userAnswers;
    private Context context;
    private AlertDialog dialog;
    private OnQuestionClickListener listener;
    
    public interface OnQuestionClickListener {
        void onQuestionClick(int position);
    }
    
    public QuestionOverviewAdapter(List<Question> questions, List<UserAnswer> userAnswers, 
                                   Context context, OnQuestionClickListener listener) {
        this.questions = questions;
        this.userAnswers = userAnswers;
        this.context = context;
        this.listener = listener;
    }
    
    public void setDialog(AlertDialog dialog) {
        this.dialog = dialog;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_question_number, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }
    
    @Override
    public int getItemCount() {
        return questions.size();
    }
    
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNumber;
        
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNumber = itemView.findViewById(R.id.tvQuestionNumber);
        }
        
        void bind(int position) {
            tvNumber.setText(String.valueOf(position + 1));
            
            UserAnswer userAnswer = userAnswers.get(position);
            String selectedAnswer = userAnswer.getSelectedAnswer();
            boolean isMarkedForReview = userAnswer.isMarkedForReview();
            
            if (isMarkedForReview) {
                tvNumber.setBackgroundColor(context.getResources().getColor(android.R.color.holo_orange_dark, null));
            } else if (selectedAnswer != null && !selectedAnswer.isEmpty()) {
                tvNumber.setBackgroundColor(context.getResources().getColor(R.color.primary, null));
            } else {
                tvNumber.setBackgroundColor(context.getResources().getColor(R.color.text_secondary, null));
            }
            
            tvNumber.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onQuestionClick(position);
                }
                if (dialog != null) {
                    dialog.dismiss();
                }
            });
        }
    }
}

