package com.p3pi27.quarters.response.user;

public class User {

    private String id, displayName, email;
    private boolean emailVerified;

    public String getUserID() {

        return id;
    }

    public String getDisplayName() {

        return displayName;
    }

    public String getEmail() {

        return email;
    }

    public boolean isEmailVerified() {

        return emailVerified;
    }
}