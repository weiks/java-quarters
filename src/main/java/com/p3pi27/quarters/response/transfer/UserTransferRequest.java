package com.p3pi27.quarters.response.transfer;

import java.util.HashMap;
import java.util.Map;

public class UserTransferRequest {

    private String id;

    public static Map<String, Object> getRequestBody(String clientID, long amount, String description) {

        Map<String, Object> body = new HashMap<>();

        body.put("appId", clientID);
        body.put("tokens", amount);
        body.put("description", description);

        return body;
    }

    public String getRequestID() {

        return id;
    }
}