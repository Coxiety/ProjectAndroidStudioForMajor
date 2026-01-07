package com.example.learningapp.fragments.pomodoro;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.learningapp.R;
import com.example.learningapp.models.PomodoroSession;

import java.util.Locale;

public class PomodoroSessionFragment extends Fragment {
    
    private TextView tvTimer, tvStatus, tvCycle;
    private Button btnPauseResume, btnEnd;
    
    private PomodoroSession session;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private boolean isRunning = false;
    private boolean isWorkTime = true;
    private int currentCycle = 1;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pomodoro_session, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        tvTimer = view.findViewById(R.id.tvTimer);
        tvStatus = view.findViewById(R.id.tvStatus);
        tvCycle = view.findViewById(R.id.tvCycle);
        btnPauseResume = view.findViewById(R.id.btnPauseResume);
        btnEnd = view.findViewById(R.id.btnEnd);
        
        Bundle args = getArguments();
        session = new PomodoroSession();
        if (args != null) {
            session.setWorkDuration(args.getInt("work_duration", 25));
            session.setShortBreakDuration(args.getInt("short_break", 5));
            session.setLongBreakDuration(args.getInt("long_break", 15));
            session.setTotalCycles(args.getInt("cycles", 4));
        }
        
        timeLeftInMillis = session.getWorkDuration() * 60 * 1000L;
        updateTimer();
        updateCycleDisplay();
        
        btnPauseResume.setOnClickListener(v -> {
            if (isRunning) {
                pauseTimer();
            } else {
                startTimer();
            }
        });
        
        btnEnd.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                .setTitle("Kết thúc phiên")
                .setMessage("Bạn có chắc muốn kết thúc phiên Pomodoro?")
                .setPositiveButton(R.string.yes, (dialog, which) -> requireActivity().onBackPressed())
                .setNegativeButton(R.string.no, null)
                .show();
        });
        
        startTimer();
        
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new androidx.activity.OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                new AlertDialog.Builder(requireContext())
                    .setTitle("Kết thúc phiên")
                    .setMessage("Bạn có chắc muốn kết thúc phiên Pomodoro?")
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        setEnabled(false);
                        requireActivity().onBackPressed();
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
            }
        });
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
                onTimerFinished();
            }
        }.start();
        
        isRunning = true;
        btnPauseResume.setText(R.string.pause);
    }
    
    private void pauseTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        isRunning = false;
        btnPauseResume.setText(R.string.resume);
    }
    
    private void updateTimer() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        
        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        tvTimer.setText(timeFormatted);
    }
    
    private void updateCycleDisplay() {
        tvCycle.setText("Chu kỳ " + currentCycle + "/" + session.getTotalCycles());
    }
    
    private void onTimerFinished() {
        if (isWorkTime) {
            Toast.makeText(requireContext(), R.string.cycle_complete, Toast.LENGTH_SHORT).show();
            isWorkTime = false;
            
            if (currentCycle == session.getTotalCycles()) {
                timeLeftInMillis = session.getLongBreakDuration() * 60 * 1000L;
            } else {
                timeLeftInMillis = session.getShortBreakDuration() * 60 * 1000L;
            }
            
            tvStatus.setText(R.string.break_time);
            tvStatus.setTextColor(requireContext().getResources().getColor(R.color.pomodoro_break, null));
        } else {
            isWorkTime = true;
            currentCycle++;
            
            if (currentCycle > session.getTotalCycles()) {
                Toast.makeText(requireContext(), "Hoàn thành tất cả chu kỳ!", Toast.LENGTH_LONG).show();
                requireActivity().onBackPressed();
                return;
            }
            
            timeLeftInMillis = session.getWorkDuration() * 60 * 1000L;
            tvStatus.setText(R.string.work_time);
            tvStatus.setTextColor(requireContext().getResources().getColor(R.color.pomodoro_work, null));
            updateCycleDisplay();
        }
        
        updateTimer();
        startTimer();
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}

