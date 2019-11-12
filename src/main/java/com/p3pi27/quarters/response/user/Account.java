package com.p3pi27.quarters.response.user;

import java.time.Instant;

public class Account {

    private String id;
    private String address;
    private String created;
    private String userId;

    public String getAccountID() {

        return id;
    }

    public String getAddress() {

        return address;
    }

    public Instant getCreted() {

        return Instant.parse(created);
    }

    public String getUserID() {

        return userId;
    }
}
