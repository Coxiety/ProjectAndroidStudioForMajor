package com.example.learningapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.learningapp.R;
import com.example.learningapp.database.DatabaseHelper;
import com.example.learningapp.models.Flashcard;
import com.example.learningapp.utils.ImageHelper;

import java.util.List;

public class FlashcardSessionActivity extends AppCompatActivity {
    
    private CardView cardFlashcard;
    private TextView tvProgress, tvCardContent, tvFlipHint;
    private ImageView ivFlashcardImage;
    private Button btnKnow, btnNotSure;
    
    private DatabaseHelper databaseHelper;
    private List<Flashcard> flashcards;
    private int currentIndex = 0;
    private boolean isFlipped = false;
    private int topicId;
    private boolean isImageOnly;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_session);
        
        topicId = getIntent().getIntExtra("topic_id", -1);
        String topicName = getIntent().getStringExtra("topic_name");
        isImageOnly = getIntent().getBooleanExtra("is_image_only", false);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(topicName);
        }
        
        cardFlashcard = findViewById(R.id.cardFlashcard);
        tvProgress = findViewById(R.id.tvProgress);
        tvCardContent = findViewById(R.id.tvCardContent);
        tvFlipHint = findViewById(R.id.tvFlipHint);
        ivFlashcardImage = findViewById(R.id.ivFlashcardImage);
        btnKnow = findViewById(R.id.btnKnow);
        btnNotSure = findViewById(R.id.btnNotSure);
        
        databaseHelper = new DatabaseHelper(this);
        flashcards = databaseHelper.getFlashcardsByTopic(topicId);
        
        if (flashcards.isEmpty()) {
            Toast.makeText(this, "Không có thẻ nào trong chủ đề này", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        java.util.Collections.shuffle(flashcards);
        
        setupListeners();
        showCurrentCard();
    }
    
    private void setupListeners() {
        cardFlashcard.setOnClickListener(v -> flipCard());
        
        btnKnow.setOnClickListener(v -> {
            if (isFlipped) {
                databaseHelper.updateFlashcardLearned(flashcards.get(currentIndex).getId(), true);
                nextCard();
            } else {
                Toast.makeText(this, "Vui lòng lật thẻ trước", Toast.LENGTH_SHORT).show();
            }
        });
        
        btnNotSure.setOnClickListener(v -> {
            if (isFlipped) {
                databaseHelper.updateFlashcardLearned(flashcards.get(currentIndex).getId(), false);
                nextCard();
            } else {
                Toast.makeText(this, "Vui lòng lật thẻ trước", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void showCurrentCard() {
        Flashcard card = flashcards.get(currentIndex);
        tvProgress.setText((currentIndex + 1) + "/" + flashcards.size());
        tvCardContent.setText(card.getFront());
        isFlipped = false;
        tvFlipHint.setVisibility(View.VISIBLE);
        
        ImageHelper.loadFlashcardImage(this, ivFlashcardImage, card.getImagePath());
    }
    
    private void flipCard() {
        Flashcard card = flashcards.get(currentIndex);
        if (!isFlipped) {
            tvCardContent.setText(card.getBack());
            isFlipped = true;
            tvFlipHint.setVisibility(View.GONE);
        } else {
            tvCardContent.setText(card.getFront());
            isFlipped = false;
            tvFlipHint.setVisibility(View.VISIBLE);
        }
    }
    
    private void nextCard() {
        currentIndex++;
        if (currentIndex >= flashcards.size()) {
            Toast.makeText(this, R.string.session_complete, Toast.LENGTH_LONG).show();
            finish();
        } else {
            showCurrentCard();
        }
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

