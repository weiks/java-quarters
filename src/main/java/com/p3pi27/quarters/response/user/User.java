package com.p3pi27.quarters.response.user;

public class User {

    private String id;
    private String displayName;
    private String email;
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
