package com.moutamid.routineapp.models;

public class StepsLocalModel {
    String name, time;
    boolean completed;

    public StepsLocalModel() {
    }

    public StepsLocalModel(String name, String time, boolean completed) {
        this.name = name;
        this.time = time;
        this.completed = completed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
