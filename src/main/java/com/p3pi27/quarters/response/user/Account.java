package com.p3pi27.quarters.response.user;

import java.time.Instant;

public class Account {

    private String id, address, created, userId;

    public String getAccountID() {

        return id;
    }

    public String getAddress() {

        return address;
    }

    public Instant getCreated() {

        return Instant.parse(created);
    }

    public String getUserID() {

        return userId;
    }
}