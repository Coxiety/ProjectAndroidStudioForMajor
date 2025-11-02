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
import com.example.learningapp.models.Question;

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
            TextView tvQuestionNumber, tvQuestion, tvCorrectAnswer;
            CardView cardView;
            
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
                tvQuestion = itemView.findViewById(R.id.tvQuestion);
                tvCorrectAnswer = itemView.findViewById(R.id.tvCorrectAnswer);
                cardView = (CardView) itemView;
            }
            
            public void bind(Question question, int number) {
                tvQuestionNumber.setText("Câu " + number);
                tvQuestion.setText(question.getQuestionText());
                tvCorrectAnswer.setText("Đáp án: " + question.getCorrectAnswer());
                
                cardView.setOnClickListener(v -> {
                    Intent intent = new Intent(ExamDetailActivity.this, QuestionDetailActivity.class);
                    intent.putExtra("question_text", question.getQuestionText());
                    intent.putExtra("option_a", question.getOptionA());
                    intent.putExtra("option_b", question.getOptionB());
                    intent.putExtra("option_c", question.getOptionC());
                    intent.putExtra("option_d", question.getOptionD());
                    intent.putExtra("correct_answer", question.getCorrectAnswer());
                    intent.putExtra("explanation", question.getExplanation());
                    intent.putExtra("image_path", question.getImagePath());
                    startActivity(intent);
                });
            }
        }
    }
}

