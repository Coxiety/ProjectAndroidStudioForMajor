package com.example.learningapp.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningapp.R;
import com.example.learningapp.database.DatabaseHelper;
import com.example.learningapp.models.Question;
import com.example.learningapp.models.UserAnswer;
import com.example.learningapp.utils.ImageHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PracticeTestActivity extends AppCompatActivity {
    
    private TextView tvTimeRemaining, tvQuestionProgress, tvQuestionNumber, tvQuestion;
    private ImageView ivQuestionImage;
    private RadioGroup radioGroupOptions;
    private RadioButton radioOptionA, radioOptionB, radioOptionC, radioOptionD;
    private CheckBox checkMarkReview;
    private Button btnPrevious, btnNext, btnSubmit, btnQuestionOverview;
    
    private DatabaseHelper databaseHelper;
    private List<Question> questions;
    private List<UserAnswer> userAnswers;
    private int currentQuestionIndex = 0;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_test);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Thi thử");
        }
        
        initializeViews();
        
        int examSetId = getIntent().getIntExtra("exam_set_id", -1);
        int numQuestions = getIntent().getIntExtra("num_questions", 30);
        int duration = getIntent().getIntExtra("duration", 45);
        
        timeLeftInMillis = duration * 60 * 1000L;
        
        databaseHelper = new DatabaseHelper(this);
        questions = databaseHelper.getRandomQuestionsForTest(examSetId, numQuestions);
        
        userAnswers = new ArrayList<>();
        for (Question question : questions) {
            UserAnswer userAnswer = new UserAnswer();
            userAnswer.setQuestionId(question.getId());
            userAnswer.setCorrectAnswer(question.getCorrectAnswer());
            userAnswers.add(userAnswer);
        }
        
        startTimer();
        displayQuestion();
        setupListeners();
    }
    
    private void initializeViews() {
        tvTimeRemaining = findViewById(R.id.tvTimeRemaining);
        tvQuestionProgress = findViewById(R.id.tvQuestionProgress);
        tvQuestionNumber = findViewById(R.id.tvQuestionNumber);
        tvQuestion = findViewById(R.id.tvQuestion);
        ivQuestionImage = findViewById(R.id.ivQuestionImage);
        radioGroupOptions = findViewById(R.id.radioGroupOptions);
        radioOptionA = findViewById(R.id.radioOptionA);
        radioOptionB = findViewById(R.id.radioOptionB);
        radioOptionC = findViewById(R.id.radioOptionC);
        radioOptionD = findViewById(R.id.radioOptionD);
        checkMarkReview = findViewById(R.id.checkMarkReview);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnNext = findViewById(R.id.btnNext);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnQuestionOverview = findViewById(R.id.btnQuestionOverview);
    }
    
    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimer();
            }
            
            @Override
            public void onFinish() {
                Toast.makeText(PracticeTestActivity.this, "Hết giờ!", Toast.LENGTH_SHORT).show();
                submitTest();
            }
        }.start();
    }
    
    private void updateTimer() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        tvTimeRemaining.setText(timeFormatted);
    }
    
    private void setupListeners() {
        btnQuestionOverview.setOnClickListener(v -> showQuestionOverviewDialog());
        
        radioGroupOptions.setOnCheckedChangeListener((group, checkedId) -> {
            String selectedAnswer = "";
            if (checkedId == R.id.radioOptionA) {
                selectedAnswer = "A";
            } else if (checkedId == R.id.radioOptionB) {
                selectedAnswer = "B";
            } else if (checkedId == R.id.radioOptionC) {
                selectedAnswer = "C";
            } else if (checkedId == R.id.radioOptionD) {
                selectedAnswer = "D";
            }
            userAnswers.get(currentQuestionIndex).setSelectedAnswer(selectedAnswer);
        });
        
        checkMarkReview.setOnCheckedChangeListener((buttonView, isChecked) -> {
            userAnswers.get(currentQuestionIndex).setMarkedForReview(isChecked);
        });
        
        btnPrevious.setOnClickListener(v -> {
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                displayQuestion();
            }
        });
        
        btnNext.setOnClickListener(v -> {
            if (currentQuestionIndex < questions.size() - 1) {
                currentQuestionIndex++;
                displayQuestion();
            }
        });
        
        btnSubmit.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                .setTitle(R.string.confirm_submit)
                .setMessage("Bạn có chắc muốn nộp bài?")
                .setPositiveButton(R.string.yes, (dialog, which) -> submitTest())
                .setNegativeButton(R.string.no, null)
                .show();
        });
    }
    
    private void displayQuestion() {
        Question question = questions.get(currentQuestionIndex);
        UserAnswer userAnswer = userAnswers.get(currentQuestionIndex);
        
        tvQuestionProgress.setText((currentQuestionIndex + 1) + "/" + questions.size());
        tvQuestionNumber.setText("Câu " + (currentQuestionIndex + 1));
        tvQuestion.setText(question.getQuestionText());
        
        // Load question image if available
        ImageHelper.loadQuestionImage(this, ivQuestionImage, question.getImagePath());
        
        radioOptionA.setText("A. " + question.getOptionA());
        radioOptionB.setText("B. " + question.getOptionB());
        
        if (question.getOptionC() != null && !question.getOptionC().isEmpty() && !question.getOptionC().equals("null")) {
            radioOptionC.setVisibility(View.VISIBLE);
            radioOptionC.setText("C. " + question.getOptionC());
        } else {
            radioOptionC.setVisibility(View.GONE);
        }
        
        if (question.getOptionD() != null && !question.getOptionD().isEmpty() && !question.getOptionD().equals("null")) {
            radioOptionD.setVisibility(View.VISIBLE);
            radioOptionD.setText("D. " + question.getOptionD());
        } else {
            radioOptionD.setVisibility(View.GONE);
        }
        
        radioGroupOptions.clearCheck();
        if (userAnswer.getSelectedAnswer() != null) {
            switch (userAnswer.getSelectedAnswer()) {
                case "A":
                    radioOptionA.setChecked(true);
                    break;
                case "B":
                    radioOptionB.setChecked(true);
                    break;
                case "C":
                    radioOptionC.setChecked(true);
                    break;
                case "D":
                    radioOptionD.setChecked(true);
                    break;
            }
        }
        
        checkMarkReview.setChecked(userAnswer.isMarkedForReview());
        
        btnPrevious.setEnabled(currentQuestionIndex > 0);
        
        if (currentQuestionIndex == questions.size() - 1) {
            btnNext.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.VISIBLE);
        } else {
            btnNext.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.GONE);
        }
    }
    
    private void submitTest() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        
        Intent intent = new Intent(this, TestResultActivity.class);
        intent.putExtra("exam_set_id", getIntent().getIntExtra("exam_set_id", -1));
        intent.putExtra("total_questions", questions.size());
        
        ArrayList<Integer> questionIds = new ArrayList<>();
        ArrayList<String> selectedAnswers = new ArrayList<>();
        ArrayList<String> correctAnswers = new ArrayList<>();
        ArrayList<Boolean> markedForReview = new ArrayList<>();
        
        for (int i = 0; i < userAnswers.size(); i++) {
            questionIds.add(questions.get(i).getId());
            selectedAnswers.add(userAnswers.get(i).getSelectedAnswer());
            correctAnswers.add(userAnswers.get(i).getCorrectAnswer());
            markedForReview.add(userAnswers.get(i).isMarkedForReview());
        }
        
        intent.putIntegerArrayListExtra("question_ids", questionIds);
        intent.putStringArrayListExtra("selected_answers", selectedAnswers);
        intent.putStringArrayListExtra("correct_answers", correctAnswers);
        intent.putExtra("marked_for_review", markedForReview);
        
        startActivity(intent);
        finish();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
    
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
            .setTitle("Thoát bài thi")
            .setMessage("Bạn có chắc muốn thoát? Kết quả sẽ không được lưu.")
            .setPositiveButton(R.string.yes, (dialog, which) -> super.onBackPressed())
            .setNegativeButton(R.string.no, null)
            .show();
    }
    
    private void showQuestionOverviewDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_question_overview, null);
        RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerViewQuestions);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 5));
        
        QuestionOverviewAdapter adapter = new QuestionOverviewAdapter();
        recyclerView.setAdapter(adapter);
        
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();
        
        adapter.setDialog(dialog);
        
        dialog.show();
    }
    
    private class QuestionOverviewAdapter extends RecyclerView.Adapter<QuestionOverviewAdapter.ViewHolder> {
        
        private AlertDialog dialog;
        
        void setDialog(AlertDialog dialog) {
            this.dialog = dialog;
        }
        
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_question_number, parent, false);
            return new ViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bind(position);
        }
        
        @Override
        public int getItemCount() {
            return questions.size();
        }
        
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvNumber;
            
            ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvNumber = itemView.findViewById(R.id.tvQuestionNumber);
            }
            
            void bind(int position) {
                tvNumber.setText(String.valueOf(position + 1));
                
                UserAnswer userAnswer = userAnswers.get(position);
                if (userAnswer.getSelectedAnswer() != null) {
                    tvNumber.setBackgroundColor(getResources().getColor(R.color.primary, null));
                } else {
                    tvNumber.setBackgroundColor(getResources().getColor(R.color.text_secondary, null));
                }
                
                tvNumber.setOnClickListener(v -> {
                    currentQuestionIndex = position;
                    displayQuestion();
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                });
            }
        }
    }
}

