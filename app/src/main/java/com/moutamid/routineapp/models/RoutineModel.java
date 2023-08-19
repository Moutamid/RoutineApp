package com.moutamid.routineapp.models;

import java.util.ArrayList;

public class RoutineModel {
    String ID, name, context;
    int minutes;
    ArrayList<String> days;
    ArrayList<AddStepsChildModel> steps;

    public RoutineModel() {
    }

    public RoutineModel(String ID, String name, String context, int minutes, ArrayList<String> days, ArrayList<AddStepsChildModel> steps) {
        this.ID = ID;
        this.name = name;
        this.context = context;
        this.minutes = minutes;
        this.days = days;
        this.steps = steps;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public ArrayList<String> getDays() {
        return days;
    }

    public void setDays(ArrayList<String> days) {
        this.days = days;
    }

    public ArrayList<AddStepsChildModel> getSteps() {
        return steps;
    }

    public void setSteps(ArrayList<AddStepsChildModel> steps) {
        this.steps = steps;
    }
}
