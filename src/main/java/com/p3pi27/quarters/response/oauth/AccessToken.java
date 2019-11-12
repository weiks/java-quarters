package com.p3pi27.quarters.response.oauth;

import java.util.HashMap;
import java.util.Map;

public class AccessToken {

    private String access_token;

    public static Map<String, String> getRequestBody(String clientID, String clientKey, String refreshToken) {

        Map<String, String> body = new HashMap<>();

        body.put("client_id", clientID);
        body.put("client_secret", clientKey);
        body.put("grant_type", "refresh_token");
        body.put("refresh_token", refreshToken);

        return body;
    }

    public String getAccessToken() {

        return access_token;
    }
}