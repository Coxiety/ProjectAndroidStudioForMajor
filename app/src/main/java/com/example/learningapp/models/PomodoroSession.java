package com.example.learningapp.models;

public class PomodoroSession {
    private int id;
    private int workDuration;
    private int shortBreakDuration;
    private int longBreakDuration;
    private int totalCycles;
    private int currentCycle;
    private long startTime;
    private long endTime;
    private boolean isCompleted;

    public PomodoroSession() {
        this.workDuration = 25;
        this.shortBreakDuration = 5;
        this.longBreakDuration = 15;
        this.totalCycles = 4;
        this.currentCycle = 0;
        this.isCompleted = false;
    }

    public PomodoroSession(int workDuration, int shortBreakDuration, 
                          int longBreakDuration, int totalCycles) {
        this.workDuration = workDuration;
        this.shortBreakDuration = shortBreakDuration;
        this.longBreakDuration = longBreakDuration;
        this.totalCycles = totalCycles;
        this.currentCycle = 0;
        this.isCompleted = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWorkDuration() {
        return workDuration;
    }

    public void setWorkDuration(int workDuration) {
        this.workDuration = workDuration;
    }

    public int getShortBreakDuration() {
        return shortBreakDuration;
    }

    public void setShortBreakDuration(int shortBreakDuration) {
        this.shortBreakDuration = shortBreakDuration;
    }

    public int getLongBreakDuration() {
        return longBreakDuration;
    }

    public void setLongBreakDuration(int longBreakDuration) {
        this.longBreakDuration = longBreakDuration;
    }

    public int getTotalCycles() {
        return totalCycles;
    }

    public void setTotalCycles(int totalCycles) {
        this.totalCycles = totalCycles;
    }

    public int getCurrentCycle() {
        return currentCycle;
    }

    public void setCurrentCycle(int currentCycle) {
        this.currentCycle = currentCycle;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public int getBreakDuration() {
        if (currentCycle % totalCycles == 0 && currentCycle > 0) {
            return longBreakDuration;
        }
        return shortBreakDuration;
    }
}

