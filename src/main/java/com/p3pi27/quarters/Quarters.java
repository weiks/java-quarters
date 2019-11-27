package com.p3pi27.quarters;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.p3pi27.quarters.response.oauth.AccessToken;
import com.p3pi27.quarters.response.oauth.RefreshToken;
import com.p3pi27.quarters.response.transfer.ServerTransferRequest;
import com.p3pi27.quarters.response.transfer.UserTransferRequest;
import com.p3pi27.quarters.response.user.Account;
import com.p3pi27.quarters.response.user.AccountBalance;
import com.p3pi27.quarters.response.user.GuestAccount;
import com.p3pi27.quarters.response.user.User;
import okhttp3.HttpUrl;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.http.*;

import java.util.Map;
import java.util.concurrent.Executor;


public class Quarters {

    private final String clientID, clientKey;
    private final Environment environment;
    private final Service service;
    private final boolean shortenURLs;

    private Quarters(String clientID, String clientKey, Environment environment, Service service, boolean shortenURLs) {

        this.clientID = clientID;
        this.clientKey = clientKey;
        this.environment = environment;
        this.service = service;
        this.shortenURLs = shortenURLs;
    }

    /**
     * Get a user's refresh and access tokens
     * @param authorizationCode User's authorization code
     * @return Retrofit {@link Call} to get {@link RefreshToken}
     */
    public Call<RefreshToken> getRefreshToken(String authorizationCode) {

        return service.getRefreshToken(RefreshToken.getRequestBody(clientID, clientKey, authorizationCode));
    }

    /**
     * Get a user's access token
     * @param refreshToken User's refresh token
     * @return Retrofit {@link Call} to get {@link AccessToken}
     */
    public Call<AccessToken> getAccessToken(String refreshToken) {

        return service.getAccessToken(AccessToken.getRequestBody(clientID, clientKey, refreshToken));
    }

    /**
     * Get a user's details
     * @param accessToken User's access token
     * @return Retrofit {@link Call} to get {@link AccessToken}
     */
    public Call<User> getUser(String accessToken) {

        return service.getUser("Bearer " + accessToken);
    }

    /**
     * Get a user's accounts - a response with many accounts is a bug in the API
     * @param accessToken User's access token
     * @return Retrofit {@link Call} to get {@link Account[]}
     */
    public Call<Account[]> getAccounts(String accessToken) {

        return service.getAccounts("Bearer " + accessToken);
    }

    /**
     * Get an account's balance
     * @param accessToken User's access token
     * @param accountAddress Account address
     * @return Retrofit {@link Call} to get {@link AccountBalance}
     */
    public Call<AccountBalance> getAccountBalance(String accessToken, String accountAddress) {

        return service.getAccountBalance("Bearer " + accessToken, accountAddress);
    }

    /**
     * Create a guest account
     * @param serverKey Server API key
     * @return Retrofit {@link Call} to get {@link GuestAccount}
     */
    public Call<GuestAccount> createGuestAccount(String serverKey) {

        return service.createGuestAccount("Bearer " + serverKey);
    }

    /**
     * Request the transfer of Quarters from a user's account to the app's account (for purchases)
     * @param accessToken User's access token
     * @param amount Amount of Quarters to transfer
     * @param description Description of purchase (can be null)
     * @return Retrofit {@link Call} to get {@link UserTransferRequest}
     */
    public Call<UserTransferRequest> requestUserTransfer(String accessToken, long amount, String description) {

        return service.requestUserTransfer("Bearer " + accessToken,
                UserTransferRequest.getRequestBody(clientID, amount, description));
    }

    /**
     * Request the transfer of Quarters from the app's account to a user's account
     * @param serverKey Server API key
     * @param appAccountAddress Server account address
     * @param amount Amount of Quarters to transfer
     * @param userID User ID (only one of this and accountAddress is needed)
     * @param accountAddress User account address (only one of this and userID is needed)
     * @return Retrofit {@link Call} to get {@link ServerTransferRequest}
     */
    public Call<ServerTransferRequest> requestServerTransfer(String serverKey, String appAccountAddress, long amount,
                                                             String userID, String accountAddress) {

        return service.requestServerTransfer("Bearer " + serverKey, appAccountAddress,
                ServerTransferRequest.getRequestBody(amount, userID, accountAddress));
    }

    /**
     * Get URL allowing users to authorize app
     * @param redirectURL URL to redirect users to after authorization (can be null)
     * @return URL to send users to in order for them to authorize app
     */
    public HttpUrl getAuthorizationURL(String redirectURL) {

        return environment.getQuartersURL(shortenURLs).newBuilder().addPathSegments("oauth/authorize")
                .addQueryParameter("response_type", "code")
                .addQueryParameter("inline", "true")
                .addQueryParameter("client_id", clientID)
                .addQueryParameter("redirect_uri", redirectURL)
                .build();
    }

    /**
     * Get URL allowing guest users to sign up for full account
     * @param accessToken Guest user's access token
     * @param redirectURL URL to redirect users to after sign-up (can be null)
     * @return URL to send guest users to in order for them to register for full account
     */
    public HttpUrl getGuestSignupURL(String accessToken, String redirectURL) {

        return environment.getQuartersURL(shortenURLs).newBuilder().addPathSegments("guest")
                .addQueryParameter("response_type", "code")
                .addQueryParameter("inline", "true")
                .addQueryParameter("client_id", clientID)
                .addQueryParameter("token", accessToken)
                .addQueryParameter("redirect_uri", redirectURL)
                .build();
    }

    /**
     * Get URL allowing users to allow transfer of Quarters from their account (for purchases)
     *
     * @param requestID     Request ID (see {@link #requestUserTransfer(String, long, String)})
     * @param firebaseToken Firebase token (for guest accounts only)
     * @return URL to send users to in order for them to approve transfer
     */
    public HttpUrl getTransferAuthorizationURL(String requestID, String firebaseToken) {

        return environment.getQuartersURL(shortenURLs).newBuilder().addPathSegments("requests/" + requestID)
                .addQueryParameter("inline", "true")
                .addQueryParameter("firebase_token", firebaseToken)
                .build();
    }

    /**
     * Get URL showing users their access and refresh tokens for app
     * @return URL to send users to in order for them to see tokens
     */
    public HttpUrl getTokenURL() {

        return environment.getQuartersURL(shortenURLs).newBuilder().addPathSegments("access-token")
                .addQueryParameter("app_id", clientID)
                .addQueryParameter("app_key", clientKey)
                .build();
    }

    /**
     * @return Quarters environment
     */
    public Environment getEnvironment() {

        return environment;
    }

    public enum Environment {

        /**
         * Default environment
         */
        PRODUCTION(null),
        /**
         * Development environment
         */
        DEVELOPMENT("dev"),
        /**
         * Sandbox environment
         */
        SANDBOX("sandbox");

        private final HttpUrl shortQuartersURL, longQuartersURL, apiURL;

        Environment(String prefix) {

            shortQuartersURL = new HttpUrl.Builder().scheme("https")
                    .host((prefix == null ? "www" : prefix) + ".poq.gg").build();

            longQuartersURL = new HttpUrl.Builder().scheme("https")
                    .host((prefix == null ? "www" : prefix) + ".pocketfulofquarters.com").build();

            apiURL = new HttpUrl.Builder().scheme("https")
                    .host("api" + (prefix == null ? "." : "." + prefix + ".") + "pocketfulofquarters.com")
                    .addPathSegment("v1").build();
        }

        /**
         * @return Long Quarters URL
         */
        public HttpUrl getQuartersURL() {

            return getQuartersURL(false);
        }

        /**
         * @param shorten Whether to shorten URL or not
         * @return Quarters URL
         */
        public HttpUrl getQuartersURL(boolean shorten) {

            if (shorten) {

                return shortQuartersURL;

            } else {

                return longQuartersURL;
            }
        }

        /**
         * @return Quarters API URL
         */
        public HttpUrl getApiURL() {

            return apiURL;
        }
    }

    private interface Service {

        @POST("oauth/token")
        Call<RefreshToken> getRefreshToken(@Body Map<String, Object> body);

        @POST("oauth/token")
        Call<AccessToken> getAccessToken(@Body Map<String, Object> body);

        @GET("me")
        Call<User> getUser(@Header("Authorization") String authorization);

        @GET("accounts")
        Call<Account[]> getAccounts(@Header("Authorization") String authorization);

        @GET("accounts/{account-address}/balance")
        Call<AccountBalance> getAccountBalance(@Header("Authorization") String authorization,
                                               @Path("account-address") String accountAddress);

        @POST("new-guest")
        Call<GuestAccount> createGuestAccount(@Header("Authorization") String authorization);

        @POST("requests")
        Call<UserTransferRequest> requestUserTransfer(@Header("Authorization") String authorization,
                                                      @Body Map<String, Object> body);

        @POST("accounts/{app-account-address}/transfer")
        Call<ServerTransferRequest> requestServerTransfer(@Header("Authorization") String authorization,
                                                          @Path("app-account-address") String appAccountAddress,
                                                          @Body Map<String, Object> body);
    }

    public static class Builder {

        private final String clientID, clientKey;
        private Environment environment = Environment.PRODUCTION;
        private final Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        private boolean shortenURLs = false;

        /**
         * Construct builder instance
         *
         * @param clientID  App ID
         * @param clientKey App secret
         */
        public Builder(String clientID, String clientKey) {

            this.clientID = clientID;
            this.clientKey = clientKey;
        }

        /**
         * Set the app's environment (default: PRODUCTION)
         *
         * @param environment Environment to use
         * @return Modified builder instance
         */
        public Builder environment(Environment environment) {

            this.environment = environment;
            return this;
        }

        /**
         * Set the executor for response callbacks
         *
         * @param callbackExecutor Executor to use
         * @return Modified builder instance
         */
        public Builder callbackExecutor(Executor callbackExecutor) {

            retrofitBuilder.callbackExecutor(callbackExecutor);
            return this;
        }

        /**
         * Set whether to shorten Quarters URLs
         *
         * @param shortenURLs Whether to shorten or not
         * @return Modified builder instance
         */
        public Builder shortenURLs(boolean shortenURLs) {

            this.shortenURLs = shortenURLs;
            return this;
        }

        public Quarters build() {

            return new Quarters(clientID, clientKey, environment, retrofitBuilder
                    .baseUrl(environment.getApiURL() + "/")
                    .addConverterFactory(JacksonConverterFactory.create(
                            new ObjectMapper()
                                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                                    .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY)))
                    .build().create(Service.class), shortenURLs);
        }
    }
}