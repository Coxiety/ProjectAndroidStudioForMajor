package com.example.learningapp.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.learningapp.R;
import com.example.learningapp.models.PomodoroSession;

import java.util.Locale;

public class PomodoroSessionActivity extends AppCompatActivity {
    
    private TextView tvTimer, tvStatus, tvCycle;
    private Button btnPauseResume, btnEnd;
    
    private PomodoroSession session;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private boolean isRunning = false;
    private boolean isWorkTime = true;
    private int currentCycle = 1;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pomodoro_session);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Pomodoro");
        }
        
        tvTimer = findViewById(R.id.tvTimer);
        tvStatus = findViewById(R.id.tvStatus);
        tvCycle = findViewById(R.id.tvCycle);
        btnPauseResume = findViewById(R.id.btnPauseResume);
        btnEnd = findViewById(R.id.btnEnd);
        
        session = new PomodoroSession();
        session.setWorkDuration(getIntent().getIntExtra("work_duration", 25));
        session.setShortBreakDuration(getIntent().getIntExtra("short_break", 5));
        session.setLongBreakDuration(getIntent().getIntExtra("long_break", 15));
        session.setTotalCycles(getIntent().getIntExtra("cycles", 4));
        
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
            new AlertDialog.Builder(this)
                .setTitle("Kết thúc phiên")
                .setMessage("Bạn có chắc muốn kết thúc phiên Pomodoro?")
                .setPositiveButton(R.string.yes, (dialog, which) -> finish())
                .setNegativeButton(R.string.no, null)
                .show();
        });
        
        startTimer();
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
            Toast.makeText(this, R.string.cycle_complete, Toast.LENGTH_SHORT).show();
            isWorkTime = false;
            
            if (currentCycle == session.getTotalCycles()) {
                timeLeftInMillis = session.getLongBreakDuration() * 60 * 1000L;
            } else {
                timeLeftInMillis = session.getShortBreakDuration() * 60 * 1000L;
            }
            
            tvStatus.setText(R.string.break_time);
            tvStatus.setTextColor(getResources().getColor(R.color.pomodoro_break, null));
        } else {
            isWorkTime = true;
            currentCycle++;
            
            if (currentCycle > session.getTotalCycles()) {
                Toast.makeText(this, "Hoàn thành tất cả chu kỳ!", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
            
            timeLeftInMillis = session.getWorkDuration() * 60 * 1000L;
            tvStatus.setText(R.string.work_time);
            tvStatus.setTextColor(getResources().getColor(R.color.pomodoro_work, null));
            updateCycleDisplay();
        }
        
        updateTimer();
        startTimer();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

