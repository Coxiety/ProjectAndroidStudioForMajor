package com.example.learningapp.adapters;

import android.content.Context;
import android.graphics.Color;
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

import java.util.List;

public class QuestionOverviewAdapter extends RecyclerView.Adapter<QuestionOverviewAdapter.ViewHolder> {

    private List<Question> questions;
    private List<UserAnswer> userAnswers;
    private Context context;
    private OnQuestionClickListener listener;
    private AlertDialog dialog;

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
        UserAnswer userAnswer = userAnswers.get(position);
        holder.tvQuestionNumber.setText(String.valueOf(position + 1));

        // Màu sắc theo trạng thái
        if (userAnswer.isMarkedForReview()) {
            // Câu đánh dấu xem lại: Cam
            holder.tvQuestionNumber.setBackgroundColor(Color.parseColor("#FF9800"));
            holder.tvQuestionNumber.setTextColor(Color.WHITE);
        } else if (userAnswer.getSelectedAnswer() != null) {
            // Câu đã trả lời: Xanh dương
            holder.tvQuestionNumber.setBackgroundColor(Color.parseColor("#2196F3"));
            holder.tvQuestionNumber.setTextColor(Color.WHITE);
        } else {
            // Câu chưa trả lời: Xám
            holder.tvQuestionNumber.setBackgroundColor(Color.parseColor("#E0E0E0"));
            holder.tvQuestionNumber.setTextColor(Color.parseColor("#757575"));
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onQuestionClick(position);
            }
            if (dialog != null) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvQuestionNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
        }
    }
}
