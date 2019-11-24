package com.p3pi27.quarters.response.transfer;

import java.util.HashMap;
import java.util.Map;

public class ServerTransferRequest {

    private String requestId, txId;

    public static Map<String, Object> getRequestBody(long amount, String userID, String accountAddress) {

        Map<String, Object> body = new HashMap<>();

        body.put("amount", amount);
        body.put("user", userID);
        body.put("address", accountAddress);

        return body;
    }

    public String getRequestID() {

        return requestId;
    }

    public String getTransactionHash() {

        return txId;
    }
}