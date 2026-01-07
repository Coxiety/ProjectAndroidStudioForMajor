package com.example.learningapp.fragments.thithu;

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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningapp.R;
import com.example.learningapp.adapters.QuestionOverviewAdapter;
import com.example.learningapp.database.DatabaseHelper;
import com.example.learningapp.models.Question;
import com.example.learningapp.models.UserAnswer;
import com.example.learningapp.utils.ImageHelper;
import com.example.learningapp.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PracticeTestFragment extends Fragment {
    
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
    
    private RadioGroup.OnCheckedChangeListener radioGroupListener;
    private CheckBox.OnCheckedChangeListener checkBoxListener;
    
    // Utility method to convert radio button ID to answer letter
    private String getAnswerFromRadioId(int checkedId) {
        if (checkedId == R.id.radioOptionA) return "A";
        if (checkedId == R.id.radioOptionB) return "B";
        if (checkedId == R.id.radioOptionC) return "C";
        if (checkedId == R.id.radioOptionD) return "D";
        return null;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_practice_test, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        initializeViews(view);
        
        Bundle args = getArguments();
        if (args == null) return;
        
        int examSetId = args.getInt("exam_set_id", -1);
        int numQuestions = args.getInt("num_questions", 25);
        int duration = args.getInt("duration", 19);
        
        timeLeftInMillis = duration * 60 * 1000L;
        
        databaseHelper = new DatabaseHelper(requireContext());
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
        
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new androidx.activity.OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(requireContext())
                    .setTitle("Thoát bài thi")
                    .setMessage("Bạn có chắc muốn thoát? Kết quả sẽ không được lưu.")
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        setEnabled(false);
                        requireActivity().onBackPressed();
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
            }
        });
    }
    
    private void initializeViews(View view) {
        tvTimeRemaining = view.findViewById(R.id.tvTimeRemaining);
        tvQuestionProgress = view.findViewById(R.id.tvQuestionProgress);
        tvQuestionNumber = view.findViewById(R.id.tvQuestionNumber);
        tvQuestion = view.findViewById(R.id.tvQuestion);
        ivQuestionImage = view.findViewById(R.id.ivQuestionImage);
        radioGroupOptions = view.findViewById(R.id.radioGroupOptions);
        radioOptionA = view.findViewById(R.id.radioOptionA);
        radioOptionB = view.findViewById(R.id.radioOptionB);
        radioOptionC = view.findViewById(R.id.radioOptionC);
        radioOptionD = view.findViewById(R.id.radioOptionD);
        checkMarkReview = view.findViewById(R.id.checkMarkReview);
        btnPrevious = view.findViewById(R.id.btnPrevious);
        btnNext = view.findViewById(R.id.btnNext);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnQuestionOverview = view.findViewById(R.id.btnQuestionOverview);
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
                Toast.makeText(requireContext(), "Hết giờ!", Toast.LENGTH_SHORT).show();
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
        
        radioGroupListener = (group, checkedId) -> {
            userAnswers.get(currentQuestionIndex).setSelectedAnswer(getAnswerFromRadioId(checkedId));
        };
        radioGroupOptions.setOnCheckedChangeListener(radioGroupListener);
        
        checkBoxListener = (buttonView, isChecked) -> {
            userAnswers.get(currentQuestionIndex).setMarkedForReview(isChecked);
        };
        checkMarkReview.setOnCheckedChangeListener(checkBoxListener);
        
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
            new AlertDialog.Builder(requireContext())
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
        
        ImageHelper.loadQuestionImage(requireContext(), ivQuestionImage, question.getImagePath());
        
        radioOptionA.setText("A. " + question.getOptionA());
        radioOptionB.setText("B. " + question.getOptionB());
        
        if (StringUtils.isValidOption(question.getOptionC())) {
            radioOptionC.setVisibility(View.VISIBLE);
            radioOptionC.setText("C. " + question.getOptionC());
        } else {
            radioOptionC.setVisibility(View.GONE);
        }
        
        if (StringUtils.isValidOption(question.getOptionD())) {
            radioOptionD.setVisibility(View.VISIBLE);
            radioOptionD.setText("D. " + question.getOptionD());
        } else {
            radioOptionD.setVisibility(View.GONE);
        }
        
        radioGroupOptions.setOnCheckedChangeListener(null);
        checkMarkReview.setOnCheckedChangeListener(null);
        
        radioGroupOptions.clearCheck();
        if (userAnswer.getSelectedAnswer() != null) {
            switch (userAnswer.getSelectedAnswer()) {
                case "A": radioOptionA.setChecked(true); break;
                case "B": radioOptionB.setChecked(true); break;
                case "C": radioOptionC.setChecked(true); break;
                case "D": radioOptionD.setChecked(true); break;
            }
        }
        
        checkMarkReview.setChecked(userAnswer.isMarkedForReview());
        
        radioGroupOptions.setOnCheckedChangeListener(radioGroupListener);
        checkMarkReview.setOnCheckedChangeListener(checkBoxListener);
        
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
        
        Bundle resultArgs = new Bundle();
        Bundle args = getArguments();
        if (args != null) {
            resultArgs.putInt("exam_set_id", args.getInt("exam_set_id", -1));
            resultArgs.putInt("duration", args.getInt("duration", 19));
        }
        resultArgs.putInt("total_questions", questions.size());
        
        ArrayList<Integer> questionIds = new ArrayList<>();
        ArrayList<String> selectedAnswers = new ArrayList<>();
        ArrayList<String> correctAnswers = new ArrayList<>();
        ArrayList<Boolean> markedForReview = new ArrayList<>();
        ArrayList<Boolean> isLietList = new ArrayList<>();
        
        for (int i = 0; i < userAnswers.size(); i++) {
            questionIds.add(questions.get(i).getId());
            selectedAnswers.add(userAnswers.get(i).getSelectedAnswer());
            correctAnswers.add(userAnswers.get(i).getCorrectAnswer());
            markedForReview.add(userAnswers.get(i).isMarkedForReview());
            isLietList.add(questions.get(i).isLiet());
        }
        
        resultArgs.putIntegerArrayList("question_ids", questionIds);
        resultArgs.putStringArrayList("selected_answers", selectedAnswers);
        resultArgs.putStringArrayList("correct_answers", correctAnswers);
        resultArgs.putSerializable("marked_for_review", markedForReview);
        resultArgs.putSerializable("is_liet_list", isLietList);
        
        NavController navController = Navigation.findNavController(requireView());
        navController.navigate(R.id.action_practiceTestFragment_to_testResultFragment, resultArgs);
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
    
    private void showQuestionOverviewDialog() {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_question_overview, null);
        RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerViewQuestions);
        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 5));
        
        QuestionOverviewAdapter adapter = new QuestionOverviewAdapter(
                questions, 
                userAnswers, 
                requireContext(),
                position -> {
                    currentQuestionIndex = position;
                    displayQuestion();
                }
        );
        recyclerView.setAdapter(adapter);
        
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create();
        
        adapter.setDialog(dialog);
        dialog.show();
    }
}

