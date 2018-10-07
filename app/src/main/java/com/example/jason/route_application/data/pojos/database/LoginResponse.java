package com.example.jason.route_application.data.pojos.database;

public class LoginResponse {

    private int userId;
    private boolean match;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isMatch() {
        return match;
    }

    public void setMatch(boolean match) {
        this.match = match;
    }
}
