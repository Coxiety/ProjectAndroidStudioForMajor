package com.example.learningapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningapp.R;
import com.example.learningapp.models.ExamSet;

import java.util.List;

public class ExamSetAdapter extends RecyclerView.Adapter<ExamSetAdapter.ViewHolder> {
    
    private List<ExamSet> examSets;
    private Context context;
    private NavController navController;

    public ExamSetAdapter(List<ExamSet> examSets, Context context, NavController navController) {
        this.examSets = examSets;
        this.context = context;
        this.navController = navController;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exam_set, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(examSets.get(position));
    }
    
    @Override
    public int getItemCount() {
        return examSets.size();
    }
    
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvExamName, tvExamDescription, tvQuestionCount, tvBadge;
        CardView cardView;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExamName = itemView.findViewById(R.id.tvExamName);
            tvExamDescription = itemView.findViewById(R.id.tvExamDescription);
            tvQuestionCount = itemView.findViewById(R.id.tvQuestionCount);
            tvBadge = itemView.findViewById(R.id.tvBadge);
            cardView = (CardView) itemView;
        }
        
        public void bind(ExamSet examSet) {
            tvExamName.setText(examSet.getName());
            tvExamDescription.setText(examSet.getDescription());
            tvQuestionCount.setText(examSet.getQuestionCount() + " câu hỏi");
            
            if (examSet.isOfficial()) {
                tvBadge.setVisibility(View.VISIBLE);
                tvBadge.setText("Chính thức");
            } else {
                tvBadge.setVisibility(View.GONE);
            }
            
            cardView.setOnClickListener(v -> {
                Bundle args = new Bundle();
                args.putInt("exam_set_id", examSet.getId());
                args.putString("exam_name", examSet.getName());
                args.putInt("question_count", examSet.getQuestionCount());
                navController.navigate(R.id.action_examListFragment_to_examDetailFragment, args);
            });
        }
    }
}

