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
import com.example.learningapp.models.ExamSet;

import java.util.List;

public class ExamListFragment extends Fragment {
    
    private RecyclerView recyclerViewExams;
    private DatabaseHelper databaseHelper;
    private List<ExamSet> examSets;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_exam_list, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        recyclerViewExams = view.findViewById(R.id.recyclerViewExams);
        recyclerViewExams.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        databaseHelper = new DatabaseHelper(requireContext());
        loadExamSets();
    }
    
    private void loadExamSets() {
        examSets = databaseHelper.getAllExamSets();
        ExamSetsAdapter adapter = new ExamSetsAdapter(examSets);
        recyclerViewExams.setAdapter(adapter);
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
                    
                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.action_examListFragment_to_examDetailFragment, args);
                });
            }
        }
    }
}

