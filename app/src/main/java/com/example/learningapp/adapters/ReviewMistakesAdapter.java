package com.example.learningapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningapp.R;
import com.example.learningapp.models.Question;
import com.example.learningapp.utils.ImageHelper;

import java.util.List;

public class ReviewMistakesAdapter extends RecyclerView.Adapter<ReviewMistakesAdapter.ViewHolder> {
    
    private List<ReviewItem> items;
    private Context context;
    
    public ReviewMistakesAdapter(List<ReviewItem> items, Context context) {
        this.items = items;
        this.context = context;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review_question, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(items.get(position));
    }
    
    @Override
    public int getItemCount() {
        return items.size();
    }
    
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionNumber, tvLietBadge, tvResultBadge, tvQuestion, tvYourAnswer, tvCorrectAnswer;
        ImageView ivQuestionImage;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
            tvLietBadge = itemView.findViewById(R.id.tvLietBadge);
            tvResultBadge = itemView.findViewById(R.id.tvResultBadge);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            ivQuestionImage = itemView.findViewById(R.id.ivQuestionImage);
            tvYourAnswer = itemView.findViewById(R.id.tvYourAnswer);
            tvCorrectAnswer = itemView.findViewById(R.id.tvCorrectAnswer);
        }
        
        public void bind(ReviewItem item) {
            tvQuestionNumber.setText("Câu " + item.questionNumber);
            tvQuestion.setText(item.question.getQuestionText());
            
            if (item.isLiet) {
                tvLietBadge.setVisibility(View.VISIBLE);
            } else {
                tvLietBadge.setVisibility(View.GONE);
            }
            
            ImageHelper.loadQuestionImage(context, ivQuestionImage, item.question.getImagePath());
            
            boolean isCorrect = item.selectedAnswer != null && item.selectedAnswer.equals(item.correctAnswer);
            
            if (isCorrect) {
                tvResultBadge.setText("Đúng");
                tvResultBadge.setBackgroundColor(context.getResources().getColor(R.color.success, null));
                tvYourAnswer.setTextColor(context.getResources().getColor(R.color.success, null));
            } else {
                tvResultBadge.setText("Sai");
                tvResultBadge.setBackgroundColor(context.getResources().getColor(R.color.error, null));
                tvYourAnswer.setTextColor(context.getResources().getColor(R.color.error, null));
            }
            
            String selectedText = item.selectedAnswer != null ? item.selectedAnswer : "Không trả lời";
            tvYourAnswer.setText("Bạn chọn: " + selectedText);
            tvCorrectAnswer.setText("Đáp án đúng: " + item.correctAnswer);
        }
    }
    
    public static class ReviewItem {
        public Question question;
        public int questionNumber;
        public String selectedAnswer;
        public String correctAnswer;
        public boolean isLiet;
        
        public ReviewItem(Question question, int questionNumber, String selectedAnswer, String correctAnswer, boolean isLiet) {
            this.question = question;
            this.questionNumber = questionNumber;
            this.selectedAnswer = selectedAnswer;
            this.correctAnswer = correctAnswer;
            this.isLiet = isLiet;
        }
    }
}

