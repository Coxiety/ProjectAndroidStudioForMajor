package com.example.learningapp.models;

public class ExamHistory {
    private int id;
    private int examSetId;
    private String examName;
    private int totalQuestions;
    private int correctAnswers;
    private int wrongAnswers;
    private int score;
    private boolean isPassed;
    private long testDate;
    private int durationMinutes;
    private String answersJson;

    public ExamHistory() {
    }

    public ExamHistory(int id, int examSetId, String examName, int totalQuestions, 
                      int correctAnswers, int wrongAnswers, int score, boolean isPassed, 
                      long testDate, int durationMinutes, String answersJson) {
        this.id = id;
        this.examSetId = examSetId;
        this.examName = examName;
        this.totalQuestions = totalQuestions;
        this.correctAnswers = correctAnswers;
        this.wrongAnswers = wrongAnswers;
        this.score = score;
        this.isPassed = isPassed;
        this.testDate = testDate;
        this.durationMinutes = durationMinutes;
        this.answersJson = answersJson;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getExamSetId() {
        return examSetId;
    }

    public void setExamSetId(int examSetId) {
        this.examSetId = examSetId;
    }

    public String getExamName() {
        return examName;
    }

    public void setExamName(String examName) {
        this.examName = examName;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

    public void setCorrectAnswers(int correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public int getWrongAnswers() {
        return wrongAnswers;
    }

    public void setWrongAnswers(int wrongAnswers) {
        this.wrongAnswers = wrongAnswers;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isPassed() {
        return isPassed;
    }

    public void setPassed(boolean passed) {
        isPassed = passed;
    }

    public long getTestDate() {
        return testDate;
    }

    public void setTestDate(long testDate) {
        this.testDate = testDate;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getAnswersJson() {
        return answersJson;
    }

    public void setAnswersJson(String answersJson) {
        this.answersJson = answersJson;
    }
}

