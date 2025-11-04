package com.example.learningapp.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningapp.R;
import com.example.learningapp.database.DatabaseHelper;
import com.example.learningapp.models.Question;
import com.example.learningapp.utils.ImageHelper;

import java.util.ArrayList;
import java.util.List;

public class ReviewMistakesActivity extends AppCompatActivity {
    
    private RecyclerView recyclerViewReview;
    private Button btnFilterWrong, btnFilterMarked;
    private DatabaseHelper databaseHelper;
    
    private ArrayList<Integer> questionIds;
    private ArrayList<String> selectedAnswers;
    private ArrayList<String> correctAnswers;
    private ArrayList<Boolean> markedForReview;
    private List<Question> allQuestions;
    private String currentFilterMode = "wrong";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_mistakes);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        recyclerViewReview = findViewById(R.id.recyclerViewReview);
        recyclerViewReview.setLayoutManager(new LinearLayoutManager(this));
        btnFilterWrong = findViewById(R.id.btnFilterWrong);
        btnFilterMarked = findViewById(R.id.btnFilterMarked);
        
        questionIds = getIntent().getIntegerArrayListExtra("question_ids");
        selectedAnswers = getIntent().getStringArrayListExtra("selected_answers");
        correctAnswers = getIntent().getStringArrayListExtra("correct_answers");
        markedForReview = (ArrayList<Boolean>) getIntent().getSerializableExtra("marked_for_review");
        currentFilterMode = getIntent().getStringExtra("filter_mode");
        
        if (currentFilterMode == null) {
            currentFilterMode = "wrong";
        }
        
        databaseHelper = new DatabaseHelper(this);
        loadQuestions();
        
        btnFilterWrong.setOnClickListener(v -> {
            currentFilterMode = "wrong";
            displayFilteredQuestions();
        });
        
        btnFilterMarked.setOnClickListener(v -> {
            currentFilterMode = "marked";
            displayFilteredQuestions();
        });
        
        displayFilteredQuestions();
    }
    
    private void loadQuestions() {
        allQuestions = new ArrayList<>();
        for (int questionId : questionIds) {
            int examSetId = getIntent().getIntExtra("exam_set_id", -1);
            List<Question> questions = databaseHelper.getQuestionsByExamSet(examSetId);
            for (Question q : questions) {
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
            
            if ("all".equals(currentFilterMode)) {
                include = true;
            } else if ("wrong".equals(currentFilterMode)) {
                String selected = selectedAnswers.get(i);
                String correct = correctAnswers.get(i);
                include = selected == null || !selected.equals(correct);
            } else if ("marked".equals(currentFilterMode)) {
                include = markedForReview != null && markedForReview.get(i);
            }
            
            if (include) {
                ReviewItem item = new ReviewItem();
                item.question = allQuestions.get(i);
                item.questionNumber = i + 1;
                item.selectedAnswer = selectedAnswers.get(i);
                item.correctAnswer = correctAnswers.get(i);
                filteredItems.add(item);
            }
        }
        
        ReviewAdapter adapter = new ReviewAdapter(filteredItems);
        recyclerViewReview.setAdapter(adapter);
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    
    private static class ReviewItem {
        Question question;
        int questionNumber;
        String selectedAnswer;
        String correctAnswer;
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
            ReviewItem item = items.get(position);
            holder.bind(item);
        }
        
        @Override
        public int getItemCount() {
            return items.size();
        }
        
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvQuestionNumber, tvResultBadge, tvQuestion, tvYourAnswer, tvCorrectAnswer;
            ImageView ivQuestionImage;
            
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
                tvResultBadge = itemView.findViewById(R.id.tvResultBadge);
                tvQuestion = itemView.findViewById(R.id.tvQuestion);
                ivQuestionImage = itemView.findViewById(R.id.ivQuestionImage);
                tvYourAnswer = itemView.findViewById(R.id.tvYourAnswer);
                tvCorrectAnswer = itemView.findViewById(R.id.tvCorrectAnswer);
            }
            
            public void bind(ReviewItem item) {
                tvQuestionNumber.setText("Câu " + item.questionNumber);
                tvQuestion.setText(item.question.getQuestionText());
                
                ImageHelper.loadQuestionImage(ReviewMistakesActivity.this, ivQuestionImage, item.question.getImagePath());
                
                boolean isCorrect = item.selectedAnswer != null && 
                                   item.selectedAnswer.equals(item.correctAnswer);
                
                if (isCorrect) {
                    tvResultBadge.setText("Đúng");
                    tvResultBadge.setBackgroundColor(getResources().getColor(R.color.success, null));
                    tvYourAnswer.setTextColor(getResources().getColor(R.color.success, null));
                } else {
                    tvResultBadge.setText("Sai");
                    tvResultBadge.setBackgroundColor(getResources().getColor(R.color.error, null));
                    tvYourAnswer.setTextColor(getResources().getColor(R.color.error, null));
                }
                
                String selectedText = item.selectedAnswer != null ? item.selectedAnswer : "Không trả lời";
                tvYourAnswer.setText("Bạn chọn: " + selectedText);
                tvCorrectAnswer.setText("Đáp án đúng: " + item.correctAnswer);
            }
        }
    }
}

