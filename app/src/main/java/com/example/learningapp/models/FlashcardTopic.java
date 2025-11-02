package com.example.learningapp.models;

public class FlashcardTopic {
    private int id;
    private String name;
    private String description;
    private int totalCards;
    private int learnedCards;
    private boolean isImageOnly;

    public FlashcardTopic() {
    }

    public FlashcardTopic(int id, String name, String description, int totalCards, int learnedCards) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.totalCards = totalCards;
        this.learnedCards = learnedCards;
        this.isImageOnly = false;
    }
    
    public FlashcardTopic(int id, String name, String description, int totalCards, int learnedCards, boolean isImageOnly) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.totalCards = totalCards;
        this.learnedCards = learnedCards;
        this.isImageOnly = isImageOnly;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTotalCards() {
        return totalCards;
    }

    public void setTotalCards(int totalCards) {
        this.totalCards = totalCards;
    }

    public int getLearnedCards() {
        return learnedCards;
    }

    public void setLearnedCards(int learnedCards) {
        this.learnedCards = learnedCards;
    }

    public int getProgress() {
        if (totalCards == 0) return 0;
        return (learnedCards * 100) / totalCards;
    }
    
    public boolean isImageOnly() {
        return isImageOnly;
    }
    
    public void setImageOnly(boolean imageOnly) {
        isImageOnly = imageOnly;
    }
}

