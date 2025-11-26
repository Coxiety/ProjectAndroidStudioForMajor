package com.example.learningapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningapp.R;
import com.example.learningapp.database.DatabaseHelper;
import com.example.learningapp.models.Question;
import com.example.learningapp.utils.ImageHelper;

import java.util.List;

public class ExamDetailActivity extends AppCompatActivity {
    
    private TextView tvExamName, tvQuestionCount;
    private RecyclerView recyclerViewQuestions;
    private DatabaseHelper databaseHelper;
    private List<Question> questions;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_detail);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        tvExamName = findViewById(R.id.tvExamName);
        tvQuestionCount = findViewById(R.id.tvQuestionCount);
        recyclerViewQuestions = findViewById(R.id.recyclerViewQuestions);
        recyclerViewQuestions.setLayoutManager(new LinearLayoutManager(this));
        
        int examSetId = getIntent().getIntExtra("exam_set_id", -1);
        String examName = getIntent().getStringExtra("exam_name");
        int questionCount = getIntent().getIntExtra("question_count", 0);
        
        tvExamName.setText(examName);
        tvQuestionCount.setText(questionCount + " câu hỏi");
        
        databaseHelper = new DatabaseHelper(this);
        loadQuestions(examSetId);
    }
    
    private void loadQuestions(int examSetId) {
        questions = databaseHelper.getQuestionsByExamSet(examSetId);
        QuestionsAdapter adapter = new QuestionsAdapter(questions);
        recyclerViewQuestions.setAdapter(adapter);
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    
    private class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {
        
        private List<Question> questions;
        
        public QuestionsAdapter(List<Question> questions) {
            this.questions = questions;
        }
        
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_question, parent, false);
            return new ViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Question question = questions.get(position);
            holder.bind(question, position + 1);
        }
        
        @Override
        public int getItemCount() {
            return questions.size();
        }
        
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvQuestionNumber, tvQuestion, tvOptionA, tvOptionB, tvOptionC, tvOptionD, tvCorrectAnswer, tvLietBadge;
            ImageView ivQuestionImage;
            CardView cardView;
            
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
                tvQuestion = itemView.findViewById(R.id.tvQuestion);
                ivQuestionImage = itemView.findViewById(R.id.ivQuestionImage);
                tvOptionA = itemView.findViewById(R.id.tvOptionA);
                tvOptionB = itemView.findViewById(R.id.tvOptionB);
                tvOptionC = itemView.findViewById(R.id.tvOptionC);
                tvOptionD = itemView.findViewById(R.id.tvOptionD);
                tvCorrectAnswer = itemView.findViewById(R.id.tvCorrectAnswer);
                tvLietBadge = itemView.findViewById(R.id.tvLietBadge);
                cardView = (CardView) itemView;
            }
            
            public void bind(Question question, int number) {
                tvQuestionNumber.setText("Câu " + number);
                tvQuestion.setText(question.getQuestionText());
                
                if (question.isLiet()) {
                    tvLietBadge.setVisibility(View.VISIBLE);
                } else {
                    tvLietBadge.setVisibility(View.GONE);
                }
                
                ImageHelper.loadQuestionImage(ExamDetailActivity.this, ivQuestionImage, question.getImagePath());
                
                tvOptionA.setText("A. " + question.getOptionA());
                tvOptionB.setText("B. " + question.getOptionB());
                
                if (question.getOptionC() != null && !question.getOptionC().isEmpty() && !question.getOptionC().equals("null")) {
                    tvOptionC.setVisibility(View.VISIBLE);
                    tvOptionC.setText("C. " + question.getOptionC());
                } else {
                    tvOptionC.setVisibility(View.GONE);
                }
                
                if (question.getOptionD() != null && !question.getOptionD().isEmpty() && !question.getOptionD().equals("null")) {
                    tvOptionD.setVisibility(View.VISIBLE);
                    tvOptionD.setText("D. " + question.getOptionD());
                } else {
                    tvOptionD.setVisibility(View.GONE);
                }
                
                tvOptionA.setBackgroundColor(Color.parseColor("#F5F5F5"));
                tvOptionB.setBackgroundColor(Color.parseColor("#F5F5F5"));
                tvOptionC.setBackgroundColor(Color.parseColor("#F5F5F5"));
                tvOptionD.setBackgroundColor(Color.parseColor("#F5F5F5"));
                
                switch (question.getCorrectAnswer()) {
                    case "A":
                        tvOptionA.setBackgroundColor(Color.parseColor("#C8E6C9"));
                        break;
                    case "B":
                        tvOptionB.setBackgroundColor(Color.parseColor("#C8E6C9"));
                        break;
                    case "C":
                        tvOptionC.setBackgroundColor(Color.parseColor("#C8E6C9"));
                        break;
                    case "D":
                        tvOptionD.setBackgroundColor(Color.parseColor("#C8E6C9"));
                        break;
                }
                
                tvCorrectAnswer.setText("Đáp án đúng: " + question.getCorrectAnswer());
                
                cardView.setOnClickListener(v -> {
                    Intent intent = new Intent(ExamDetailActivity.this, QuestionDetailActivity.class);
                    intent.putExtra("question_text", question.getQuestionText());
                    intent.putExtra("option_a", question.getOptionA());
                    intent.putExtra("option_b", question.getOptionB());
                    intent.putExtra("option_c", question.getOptionC());
                    intent.putExtra("option_d", question.getOptionD());
                    intent.putExtra("correct_answer", question.getCorrectAnswer());
                    intent.putExtra("image_path", question.getImagePath());
                    intent.putExtra("is_liet", question.isLiet());
                    startActivity(intent);
                });
            }
        }
    }
}

