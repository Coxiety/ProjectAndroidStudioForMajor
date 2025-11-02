package com.example.learningapp.models;

public class ExamSet {
    private int id;
    private String name;
    private String description;
    private int questionCount;
    private boolean isOfficial;
    private String category;

    public ExamSet() {
    }

    public ExamSet(int id, String name, String description, int questionCount, 
                  boolean isOfficial, String category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.questionCount = questionCount;
        this.isOfficial = isOfficial;
        this.category = category;
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

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    public void setOfficial(boolean official) {
        isOfficial = official;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

