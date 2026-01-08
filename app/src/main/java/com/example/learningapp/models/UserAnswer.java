package com.example.learningapp.models;

public class UserAnswer {
    private int questionId;
    private String selectedAnswer;
    private String correctAnswer;
    private boolean isCorrect;
    private boolean isMarkedForReview;

    public UserAnswer() {
    }

//    public UserAnswer(int questionId, String selectedAnswer, String correctAnswer) {
//        this.questionId = questionId;
//        this.selectedAnswer = selectedAnswer;
//        this.correctAnswer = correctAnswer;
//        this.isCorrect = selectedAnswer != null && selectedAnswer.equals(correctAnswer);
//        this.isMarkedForReview = false;
//    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
        this.isCorrect = selectedAnswer != null && selectedAnswer.equals(correctAnswer);
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public boolean isMarkedForReview() {
        return isMarkedForReview;
    }

    public void setMarkedForReview(boolean markedForReview) {
        isMarkedForReview = markedForReview;
    }
}

