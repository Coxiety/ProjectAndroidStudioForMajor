package com.example.learningapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningapp.R;
import com.example.learningapp.database.DatabaseHelper;
import com.example.learningapp.models.ExamSet;

import java.util.List;

public class ExamListActivity extends AppCompatActivity {
    
    private RecyclerView recyclerViewExams;
    private DatabaseHelper databaseHelper;
    private List<ExamSet> examSets;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_list);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        recyclerViewExams = findViewById(R.id.recyclerViewExams);
        recyclerViewExams.setLayoutManager(new LinearLayoutManager(this));
        
        databaseHelper = new DatabaseHelper(this);
        loadExamSets();
    }
    
    private void loadExamSets() {
        examSets = databaseHelper.getAllExamSets();
        ExamSetsAdapter adapter = new ExamSetsAdapter(examSets);
        recyclerViewExams.setAdapter(adapter);
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    
    private class ExamSetsAdapter extends RecyclerView.Adapter<ExamSetsAdapter.ViewHolder> {
        
        private List<ExamSet> examSets;
        
        public ExamSetsAdapter(List<ExamSet> examSets) {
            this.examSets = examSets;
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
            ExamSet examSet = examSets.get(position);
            holder.bind(examSet);
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
                    Intent intent = new Intent(ExamListActivity.this, ExamDetailActivity.class);
                    intent.putExtra("exam_set_id", examSet.getId());
                    intent.putExtra("exam_name", examSet.getName());
                    intent.putExtra("question_count", examSet.getQuestionCount());
                    startActivity(intent);
                });
            }
        }
    }
}

