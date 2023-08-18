package com.moutamid.routineapp.models;

public class UserModel {
    String ID, name, email, password, goal;

    public UserModel() {
    }

    public UserModel(String ID, String name, String email, String password, String goal) {
        this.ID = ID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.goal = goal;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
