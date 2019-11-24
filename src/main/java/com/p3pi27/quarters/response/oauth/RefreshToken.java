package com.p3pi27.quarters.response.oauth;

import java.util.HashMap;
import java.util.Map;

public class RefreshToken {

    private String refresh_token, access_token;

    public static Map<String, Object> getRequestBody(String clientID, String clientKey, String authorizationCode) {

        Map<String, Object> body = new HashMap<>();

        body.put("client_id", clientID);
        body.put("client_key", clientKey);
        body.put("grant_type", "authorization_code");
        body.put("code", authorizationCode);

        return body;
    }

    public String getRefreshToken() {

        return refresh_token;
    }

    public String getAccessToken() {

        return access_token;
    }
}