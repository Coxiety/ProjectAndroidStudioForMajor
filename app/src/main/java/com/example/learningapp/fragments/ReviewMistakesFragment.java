package com.example.learningapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningapp.R;
import com.example.learningapp.database.DatabaseHelper;
import com.example.learningapp.models.Question;
import com.example.learningapp.utils.ImageHelper;

import java.util.ArrayList;
import java.util.List;

public class ReviewMistakesFragment extends Fragment {
    
    private RecyclerView recyclerViewReview;
    private Button btnFilterAll, btnFilterWrong, btnFilterCorrect, btnFilterMarked;
    private DatabaseHelper databaseHelper;
    
    private ArrayList<Integer> questionIds;
    private ArrayList<String> selectedAnswers;
    private ArrayList<String> correctAnswers;
    private ArrayList<Boolean> markedForReview;
    private ArrayList<Boolean> isLietList;
    private List<Question> allQuestions;
    private String currentFilterMode = "wrong";
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_review_mistakes, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        recyclerViewReview = view.findViewById(R.id.recyclerViewReview);
        recyclerViewReview.setLayoutManager(new LinearLayoutManager(requireContext()));
        btnFilterAll = view.findViewById(R.id.btnFilterAll);
        btnFilterWrong = view.findViewById(R.id.btnFilterWrong);
        btnFilterCorrect = view.findViewById(R.id.btnFilterCorrect);
        btnFilterMarked = view.findViewById(R.id.btnFilterMarked);
        
        Bundle args = getArguments();
        if (args != null) {
            questionIds = args.getIntegerArrayList("question_ids");
            selectedAnswers = args.getStringArrayList("selected_answers");
            correctAnswers = args.getStringArrayList("correct_answers");
            markedForReview = (ArrayList<Boolean>) args.getSerializable("marked_for_review");
            isLietList = (ArrayList<Boolean>) args.getSerializable("is_liet_list");
            currentFilterMode = args.getString("filter_mode");
            
            if (currentFilterMode == null) {
                currentFilterMode = "wrong";
            }
            
            databaseHelper = new DatabaseHelper(requireContext());
            loadQuestions(args.getInt("exam_set_id", -1));
            
            btnFilterAll.setOnClickListener(v -> {
                currentFilterMode = "all";
                displayFilteredQuestions();
            });
            
            btnFilterWrong.setOnClickListener(v -> {
                currentFilterMode = "wrong";
                displayFilteredQuestions();
            });
            
            btnFilterCorrect.setOnClickListener(v -> {
                currentFilterMode = "correct";
                displayFilteredQuestions();
            });
            
            btnFilterMarked.setOnClickListener(v -> {
                currentFilterMode = "marked";
                displayFilteredQuestions();
            });
            
            displayFilteredQuestions();
        }
    }
    
    private void loadQuestions(int examSetId) {
        allQuestions = new ArrayList<>();
        List<Question> allExamQuestions = databaseHelper.getQuestionsByExamSet(examSetId);
        
        for (int questionId : questionIds) {
            for (Question q : allExamQuestions) {
                if (q.getId() == questionId) {
                    allQuestions.add(q);
                    break;
                }
            }
        }
    }
    
    private void displayFilteredQuestions() {
        List<ReviewItem> filteredItems = new ArrayList<>();
        
        for (int i = 0; i < allQuestions.size(); i++) {
            boolean include = false;
            String selected = selectedAnswers.get(i);
            String correct = correctAnswers.get(i);
            boolean isCorrect = selected != null && selected.equals(correct);
            
            if ("all".equals(currentFilterMode)) {
                include = true;
            } else if ("wrong".equals(currentFilterMode)) {
                include = !isCorrect;
            } else if ("correct".equals(currentFilterMode)) {
                include = isCorrect;
            } else if ("marked".equals(currentFilterMode)) {
                include = markedForReview != null && i < markedForReview.size() && markedForReview.get(i);
            }
            
            if (include) {
                boolean isLiet = isLietList != null && i < isLietList.size() && isLietList.get(i);
                filteredItems.add(new ReviewItem(allQuestions.get(i), i + 1, selected, correct, isLiet));
            }
        }
        
        ReviewAdapter adapter = new ReviewAdapter(filteredItems);
        recyclerViewReview.setAdapter(adapter);
    }
    
    private static class ReviewItem {
        Question question;
        int questionNumber;
        String selectedAnswer;
        String correctAnswer;
        boolean isLiet;
        
        public ReviewItem(Question question, int questionNumber, String selectedAnswer, String correctAnswer, boolean isLiet) {
            this.question = question;
            this.questionNumber = questionNumber;
            this.selectedAnswer = selectedAnswer;
            this.correctAnswer = correctAnswer;
            this.isLiet = isLiet;
        }
    }
    
    private class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
        
        private List<ReviewItem> items;
        
        public ReviewAdapter(List<ReviewItem> items) {
            this.items = items;
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
                
                ImageHelper.loadQuestionImage(requireContext(), ivQuestionImage, item.question.getImagePath());
                
                boolean isCorrect = item.selectedAnswer != null && item.selectedAnswer.equals(item.correctAnswer);
                
                if (isCorrect) {
                    tvResultBadge.setText("Đúng");
                    tvResultBadge.setBackgroundColor(requireContext().getResources().getColor(R.color.success, null));
                    tvYourAnswer.setTextColor(requireContext().getResources().getColor(R.color.success, null));
                } else {
                    tvResultBadge.setText("Sai");
                    tvResultBadge.setBackgroundColor(requireContext().getResources().getColor(R.color.error, null));
                    tvYourAnswer.setTextColor(requireContext().getResources().getColor(R.color.error, null));
                }
                
                String selectedText = item.selectedAnswer != null ? item.selectedAnswer : "Không trả lời";
                tvYourAnswer.setText("Bạn chọn: " + selectedText);
                tvCorrectAnswer.setText("Đáp án đúng: " + item.correctAnswer);
            }
        }
    }
}

