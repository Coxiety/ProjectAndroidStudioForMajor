package com.example.learningapp.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.learningapp.R;
import com.example.learningapp.models.FlashcardTopic;

import java.util.List;

public class FlashcardTopicAdapter extends RecyclerView.Adapter<FlashcardTopicAdapter.ViewHolder> {
    
    private List<FlashcardTopic> topics;
    private Context context;
    private NavController navController;
    
    public FlashcardTopicAdapter(List<FlashcardTopic> topics, Context context, NavController navController) {
        this.topics = topics;
        this.context = context;
        this.navController = navController;
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
                navController.navigate(R.id.action_flashcardHubFragment_to_flashcardSessionFragment, args);
            });
        }
    }
}

