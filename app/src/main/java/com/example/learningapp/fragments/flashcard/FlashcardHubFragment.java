package com.example.learningapp.fragments.flashcard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningapp.R;
import com.example.learningapp.adapters.FlashcardTopicAdapter;
import com.example.learningapp.database.DatabaseHelper;
import com.example.learningapp.models.FlashcardTopic;

import java.util.List;

public class FlashcardHubFragment extends Fragment {
    
    private RecyclerView recyclerViewTopics;
    private DatabaseHelper databaseHelper;
    private List<FlashcardTopic> topics;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_flashcard_hub, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        recyclerViewTopics = view.findViewById(R.id.recyclerViewTopics);
        recyclerViewTopics.setLayoutManager(new LinearLayoutManager(requireContext()));
        
        databaseHelper = new DatabaseHelper(requireContext());
        loadTopics();
    }
    
    private void loadTopics() {
        topics = databaseHelper.getAllFlashcardTopics();
        NavController navController = Navigation.findNavController(requireView());
        FlashcardTopicAdapter adapter = new FlashcardTopicAdapter(topics, requireContext(), navController);
        recyclerViewTopics.setAdapter(adapter);
    }
}

