package com.example.learningapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningapp.R;
import com.example.learningapp.database.DatabaseHelper;
import com.example.learningapp.models.FlashcardTopic;

import java.util.List;

public class FlashcardHubActivity extends AppCompatActivity {
    
    private RecyclerView recyclerViewTopics;
    private DatabaseHelper databaseHelper;
    private List<FlashcardTopic> topics;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_hub);
        
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        recyclerViewTopics = findViewById(R.id.recyclerViewTopics);
        recyclerViewTopics.setLayoutManager(new LinearLayoutManager(this));
        
        databaseHelper = new DatabaseHelper(this);
        loadTopics();
    }
    
    private void loadTopics() {
        topics = databaseHelper.getAllFlashcardTopics();
        TopicsAdapter adapter = new TopicsAdapter(topics);
        recyclerViewTopics.setAdapter(adapter);
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
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
            FlashcardTopic topic = topics.get(position);
            holder.bind(topic);
        }
        
        @Override
        public int getItemCount() {
            return topics.size();
        }
        
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTopicName, tvTopicDescription, tvProgress, tvPercentage;
            ProgressBar progressBar;
            CardView cardView;
            
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTopicName = itemView.findViewById(R.id.tvTopicName);
                tvTopicDescription = itemView.findViewById(R.id.tvTopicDescription);
                tvProgress = itemView.findViewById(R.id.tvProgress);
                tvPercentage = itemView.findViewById(R.id.tvPercentage);
                progressBar = itemView.findViewById(R.id.progressBar);
                cardView = (CardView) itemView;
            }
            
            public void bind(FlashcardTopic topic) {
                tvTopicName.setText(topic.getName());
                tvTopicDescription.setText(topic.getDescription());
                tvProgress.setText(topic.getLearnedCards() + "/" + topic.getTotalCards() + " thẻ");
                tvPercentage.setText(topic.getProgress() + "%");
                progressBar.setProgress(topic.getProgress());
                
                cardView.setOnClickListener(v -> {
                    Intent intent = new Intent(FlashcardHubActivity.this, FlashcardSessionActivity.class);
                    intent.putExtra("topic_id", topic.getId());
                    intent.putExtra("topic_name", topic.getName());
                    intent.putExtra("is_image_only", topic.isImageOnly());
                    startActivity(intent);
                });
            }
        }
    }
}

