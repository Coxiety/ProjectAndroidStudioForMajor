package com.example.learningapp.models;

public class Flashcard {
    private int id;
    private String front;
    private String back;
    private String explanation;
    private String imagePath;
    private int topicId;
    private boolean isLearned;
    private int reviewCount;
    private long lastReviewTime;

    public Flashcard() {
    }

    public Flashcard(int id, String front, String back, String explanation, 
                    String imagePath, int topicId, boolean isLearned, 
                    int reviewCount, long lastReviewTime) {
        this.id = id;
        this.front = front;
        this.back = back;
        this.explanation = explanation;
        this.imagePath = imagePath;
        this.topicId = topicId;
        this.isLearned = isLearned;
        this.reviewCount = reviewCount;
        this.lastReviewTime = lastReviewTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front;
    }

    public String getBack() {
        return back;
    }

    public void setBack(String back) {
        this.back = back;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public boolean isLearned() {
        return isLearned;
    }

    public void setLearned(boolean learned) {
        isLearned = learned;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public long getLastReviewTime() {
        return lastReviewTime;
    }

    public void setLastReviewTime(long lastReviewTime) {
        this.lastReviewTime = lastReviewTime;
    }
}

