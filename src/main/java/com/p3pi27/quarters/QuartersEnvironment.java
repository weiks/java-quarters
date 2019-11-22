package com.p3pi27.quarters;

public enum QuartersEnvironment {

    /**
     * Default environment
     */
    PRODUCTION("https://api.pocketfulofquarters.com/v1/", "https://www.pocketfulofquarters.com"),
    /**
     * Development environment (https://www.dev.poq.gg)
     */
    DEVELOPMENT("https://api.dev.pocketfulofquarters.com/v1/", "https://dev.pocketfulofquarters.com"),
    /**
     * Sandbox environment (https://sandbox.poq.gg)
     */
    SANDBOX("https://api.sandbox.pocketfulofquarters.com/v1/", "https://sandbox.pocketfulofquarters.com");

    /**
     * Base API URL
     */
    private final String apiURL;
    /**
     * Base Quarters URL (used for constructing user-facing authorization page URLs)
     */
    private final String quartersURL;

    QuartersEnvironment(String apiURL, String quartersURL) {

        this.apiURL = apiURL;
        this.quartersURL = quartersURL;
    }

    public String getApiURL() {

        return apiURL;
    }

    public String getQuartersURL() {

        return quartersURL;
    }
}