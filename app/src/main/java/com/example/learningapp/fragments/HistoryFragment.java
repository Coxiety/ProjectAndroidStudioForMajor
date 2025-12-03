package com.example.learningapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningapp.R;
import com.example.learningapp.database.DatabaseHelper;
import com.example.learningapp.models.ExamHistory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryFragment extends Fragment {
    
    private RecyclerView recyclerViewHistory;
    private TextView tvNoHistory;
    private DatabaseHelper databaseHelper;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_history, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        recyclerViewHistory = view.findViewById(R.id.recyclerViewHistory);
        tvNoHistory = view.findViewById(R.id.tvNoHistory);
        recyclerViewHistory.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        databaseHelper = new DatabaseHelper(requireContext());
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
            holder.bind(historyList.get(position));
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
                    tvResultBadge.setBackgroundColor(requireContext().getResources().getColor(R.color.success, null));
                } else {
                    tvResultBadge.setText(R.string.fail);
                    tvResultBadge.setBackgroundColor(requireContext().getResources().getColor(R.color.error, null));
                }
                
                cardView.setOnClickListener(v -> {
                    Bundle args = new Bundle();
                    args.putInt("history_id", history.getId());
                    args.putString("exam_name", history.getExamName());
                    args.putInt("correct_answers", history.getCorrectAnswers());
                    args.putInt("wrong_answers", history.getWrongAnswers());
                    args.putInt("total_questions", history.getTotalQuestions());
                    args.putLong("test_date", history.getTestDate());
                    args.putInt("duration", history.getDurationMinutes());
                    
                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.action_historyFragment_to_historyDetailFragment, args);
                });
            }
        }
    }
}

