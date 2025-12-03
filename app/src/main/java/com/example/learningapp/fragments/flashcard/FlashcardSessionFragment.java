package com.example.learningapp.fragments.flashcard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.learningapp.R;
import com.example.learningapp.database.DatabaseHelper;
import com.example.learningapp.models.Flashcard;
import com.example.learningapp.utils.ImageHelper;

import java.util.List;

public class FlashcardSessionFragment extends Fragment {
    
    private CardView cardFlashcard;
    private View layoutFlashcardContent;
    private TextView tvProgress, tvCardContent, tvFlipHint;
    private ImageView ivFlashcardImage;
    private Button btnKnow, btnNotSure;
    
    private DatabaseHelper databaseHelper;
    private List<Flashcard> flashcards;
    private int currentIndex = 0;
    private boolean isFlipped = false;
    private int topicId;
    private boolean isImageOnly;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_flashcard_session, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        Bundle args = getArguments();
        if (args != null) {
            topicId = args.getInt("topic_id", -1);
            isImageOnly = args.getBoolean("is_image_only", false);
        }
        
        cardFlashcard = view.findViewById(R.id.cardFlashcard);
        layoutFlashcardContent = view.findViewById(R.id.layoutFlashcardContent);
        tvProgress = view.findViewById(R.id.tvProgress);
        tvCardContent = view.findViewById(R.id.tvCardContent);
        tvFlipHint = view.findViewById(R.id.tvFlipHint);
        ivFlashcardImage = view.findViewById(R.id.ivFlashcardImage);
        btnKnow = view.findViewById(R.id.btnKnow);
        btnNotSure = view.findViewById(R.id.btnNotSure);
        
        databaseHelper = new DatabaseHelper(requireContext());
        flashcards = databaseHelper.getFlashcardsByTopic(topicId);
        
        if (flashcards.isEmpty()) {
            Toast.makeText(requireContext(), "Không có thẻ nào trong chủ đề này", Toast.LENGTH_SHORT).show();
            requireActivity().onBackPressed();
            return;
        }
        
        java.util.Collections.shuffle(flashcards);
        
        setupListeners();
        showCurrentCard();
    }
    
    private void setupListeners() {
        cardFlashcard.setOnClickListener(v -> flipCard());
        if (layoutFlashcardContent != null) {
            layoutFlashcardContent.setOnClickListener(v -> flipCard());
        }
        
        btnKnow.setOnClickListener(v -> {
            if (isFlipped) {
                databaseHelper.updateFlashcardLearned(flashcards.get(currentIndex).getId(), true);
                nextCard();
            } else {
                Toast.makeText(requireContext(), "Vui lòng lật thẻ trước", Toast.LENGTH_SHORT).show();
            }
        });
        
        btnNotSure.setOnClickListener(v -> {
            if (isFlipped) {
                databaseHelper.updateFlashcardLearned(flashcards.get(currentIndex).getId(), false);
                nextCard();
            } else {
                Toast.makeText(requireContext(), "Vui lòng lật thẻ trước", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void showCurrentCard() {
        Flashcard card = flashcards.get(currentIndex);
        tvProgress.setText((currentIndex + 1) + "/" + flashcards.size());
        tvCardContent.setText(card.getFront());
        isFlipped = false;
        tvFlipHint.setVisibility(View.VISIBLE);
        
        ImageHelper.loadFlashcardImage(requireContext(), ivFlashcardImage, card.getImagePath());
    }
    
    private void flipCard() {
        Flashcard card = flashcards.get(currentIndex);
        if (!isFlipped) {
            tvCardContent.setText(card.getBack());
            isFlipped = true;
            tvFlipHint.setVisibility(View.GONE);
            ivFlashcardImage.setVisibility(View.GONE);
        } else {
            tvCardContent.setText(card.getFront());
            isFlipped = false;
            tvFlipHint.setVisibility(View.VISIBLE);
            ImageHelper.loadFlashcardImage(requireContext(), ivFlashcardImage, card.getImagePath());
        }
    }
    
    private void nextCard() {
        currentIndex++;
        if (currentIndex >= flashcards.size()) {
            Toast.makeText(requireContext(), R.string.session_complete, Toast.LENGTH_LONG).show();
            requireActivity().onBackPressed();
        } else {
            showCurrentCard();
        }
    }
}

