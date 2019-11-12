package com.p3pi27.quarters.response.user;

public class GuestAccount {

    private String user_id;
    private String access_token;
    private String firebase_token;
    private String address;

    public String getUserID() {

        return user_id;
    }

    public String getAccessToken() {

        return access_token;
    }

    public String getFirebaseToken() {

        return firebase_token;
    }

    public String getAddress() {

        return address;
    }
}