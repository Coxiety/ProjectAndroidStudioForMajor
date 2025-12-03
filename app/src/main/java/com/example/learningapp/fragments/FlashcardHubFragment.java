package com.example.learningapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningapp.R;
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
        TopicsAdapter adapter = new TopicsAdapter(topics);
        recyclerViewTopics.setAdapter(adapter);
    }
    
    private class TopicsAdapter extends RecyclerView.Adapter<TopicsAdapter.ViewHolder> {
        
        private List<FlashcardTopic> topics;
        
        public TopicsAdapter(List<FlashcardTopic> topics) {
            this.topics = topics;
        }
        
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_flashcard_topic, parent, false);
            return new ViewHolder(view);
        }
        
        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bind(topics.get(position));
        }
        
        @Override
        public int getItemCount() {
            return topics.size();
        }
        
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTopicName, tvTopicDescription, tvProgress;
            CardView cardView;
            
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTopicName = itemView.findViewById(R.id.tvTopicName);
                tvTopicDescription = itemView.findViewById(R.id.tvTopicDescription);
                tvProgress = itemView.findViewById(R.id.tvProgress);
                cardView = (CardView) itemView;
            }
            
            public void bind(FlashcardTopic topic) {
                tvTopicName.setText(topic.getName());
                tvTopicDescription.setText(topic.getDescription());
                tvProgress.setText(topic.getTotalCards() + " câu hỏi");
                
                cardView.setOnClickListener(v -> {
                    Bundle args = new Bundle();
                    args.putInt("topic_id", topic.getId());
                    args.putString("topic_name", topic.getName());
                    args.putBoolean("is_image_only", topic.isImageOnly());
                    
                    NavController navController = Navigation.findNavController(v);
                    navController.navigate(R.id.action_flashcardHubFragment_to_flashcardSessionFragment, args);
                });
            }
        }
    }
}

