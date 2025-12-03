package com.example.learningapp.fragments.history;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningapp.R;
import com.example.learningapp.adapters.HistoryAdapter;
import com.example.learningapp.database.DatabaseHelper;
import com.example.learningapp.models.ExamHistory;

import java.util.List;

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
            NavController navController = Navigation.findNavController(requireView());
            HistoryAdapter adapter = new HistoryAdapter(historyList, requireContext(), navController);
            recyclerViewHistory.setAdapter(adapter);
        }
    }
}

