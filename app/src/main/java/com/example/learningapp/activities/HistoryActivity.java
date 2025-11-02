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
import com.example.learningapp.models.ExamHistory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryActivity extends AppCompatActivity {
    
    private RecyclerView recyclerViewHistory;
    private TextView tvNoHistory;
    private DatabaseHelper databaseHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        recyclerViewHistory = findViewById(R.id.recyclerViewHistory);
        tvNoHistory = findViewById(R.id.tvNoHistory);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(this));
        
        databaseHelper = new DatabaseHelper(this);
        loadHistory();
    }
    
    private void loadHistory() {
        List<ExamHistory> historyList = databaseHelper.getAllExamHistory();
        
        if (historyList.isEmpty()) {
            tvNoHistory.setVisibility(View.VISIBLE);
            recyclerViewHistory.setVisibility(View.GONE);
        } else {
            tvNoHistory.setVisibility(View.GONE);
            recyclerViewHistory.setVisibility(View.VISIBLE);
            HistoryAdapter adapter = new HistoryAdapter(historyList);
            recyclerViewHistory.setAdapter(adapter);
        }
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    
    private class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
        
        private List<ExamHistory> historyList;
        private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        
        public HistoryAdapter(List<ExamHistory> historyList) {
            this.historyList = historyList;
        }
        
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_exam_history, parent, false);
            return new ViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ExamHistory history = historyList.get(position);
            holder.bind(history);
        }
        
        @Override
        public int getItemCount() {
            return historyList.size();
        }
        
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvExamName, tvResultBadge, tvTestDate, tvScore;
            CardView cardView;
            
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvExamName = itemView.findViewById(R.id.tvExamName);
                tvResultBadge = itemView.findViewById(R.id.tvResultBadge);
                tvTestDate = itemView.findViewById(R.id.tvTestDate);
                tvScore = itemView.findViewById(R.id.tvScore);
                cardView = (CardView) itemView;
            }
            
            public void bind(ExamHistory history) {
                tvExamName.setText(history.getExamName());
                tvScore.setText(history.getCorrectAnswers() + "/" + history.getTotalQuestions());
                
                String dateStr = dateFormat.format(new Date(history.getTestDate()));
                tvTestDate.setText("Ngày: " + dateStr);
                
                if (history.isPassed()) {
                    tvResultBadge.setText(R.string.pass);
                    tvResultBadge.setBackgroundColor(getResources().getColor(R.color.success, null));
                } else {
                    tvResultBadge.setText(R.string.fail);
                    tvResultBadge.setBackgroundColor(getResources().getColor(R.color.error, null));
                }
                
                cardView.setOnClickListener(v -> {
                    Intent intent = new Intent(HistoryActivity.this, HistoryDetailActivity.class);
                    intent.putExtra("history_id", history.getId());
                    intent.putExtra("exam_name", history.getExamName());
                    intent.putExtra("correct_answers", history.getCorrectAnswers());
                    intent.putExtra("wrong_answers", history.getWrongAnswers());
                    intent.putExtra("total_questions", history.getTotalQuestions());
                    intent.putExtra("test_date", history.getTestDate());
                    intent.putExtra("duration", history.getDurationMinutes());
                    startActivity(intent);
                });
            }
        }
    }
}

