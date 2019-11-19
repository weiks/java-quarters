package com.p3pi27.quarters;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.p3pi27.quarters.response.oauth.AccessToken;
import com.p3pi27.quarters.response.oauth.RefreshToken;
import com.p3pi27.quarters.response.transfer.TransferRequest;
import com.p3pi27.quarters.response.user.Account;
import com.p3pi27.quarters.response.user.AccountBalance;
import com.p3pi27.quarters.response.user.GuestAccount;
import com.p3pi27.quarters.response.user.User;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class Quarters {

    private final String clientID;
    private final String clientKey;

    private QuartersEnvironment environment;
    private QuartersService service;

    /**
     * Creates new Quarters instance
     *
     * @param clientID    Your app ID
     * @param clientKey   Your app key
     * @param environment Quarters environment - changes the base API URL
     */
    public Quarters(String clientID, String clientKey, QuartersEnvironment environment) {

        this.clientID = clientID;
        this.clientKey = clientKey;

        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        this.environment = environment;
        service = new Retrofit.Builder()
                .baseUrl(environment.getApiURL())
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .build()
                .create(QuartersService.class);
    }

    /**
     * Get a user's refresh and access token
     *
     * @param authorizationCode Code received after user authorizes app to access their account
     * @return Invocation of method to get {@link RefreshToken} (containing access and refresh token)
     */
    public Call<RefreshToken> getRefreshToken(String authorizationCode) {

        return service.getRefreshToken(RefreshToken.getRequestBody(clientID, clientKey, authorizationCode));

    }

    /**
     * Get a user's access token
     *
     * @param refreshToken User's refresh token
     * @return Invocation of method to get {@link AccessToken}
     */
    public Call<AccessToken> getAccessToken(String refreshToken) {

        return service.getAccessToken(AccessToken.getRequestBody(clientID, clientKey, refreshToken));
    }

    /**
     * Get a user's details
     *
     * @param accessToken User's access token
     * @return Invocation of method to get {@link User}
     */
    public Call<User> getUser(String accessToken) {

        return service.getUser("Bearer " + accessToken);
    }

    /**
     * Get a user's account - a response with multiple accounts demonstrates a small bug in the API
     *
     * @param accessToken User's access token
     * @return Invocation of method to get {@link Account[]}
     */
    public Call<Account[]> getAccounts(String accessToken) {

        return service.getAccounts("Bearer " + accessToken);
    }

    /**
     * Get the balance of a user's account
     *
     * @param accessToken    User's access token
     * @param accountAddress Account Etherum address
     * @return Invocation of method to get {@link AccountBalance}
     */
    public Call<AccountBalance> getAccountBalance(String accessToken, String accountAddress) {

        return service.getAccountBalance("Bearer " + accessToken, accountAddress);
    }

    /**
     * Create a guest account
     *
     * @param serverKey App's server API key
     * @return Invocation of method to get {@link GuestAccount}
     */
    public Call<GuestAccount> createGuestAccount(String serverKey) {

        return service.createGuestAccount("Bearer " + serverKey);
    }

    /**
     * Request a transfer of Quarters from a user's account
     *
     * @param accessToken User's access token
     * @param amount      Amount to transfer
     * @return Invocation of method to get {@link TransferRequest}
     */
    public Call<TransferRequest> requestTransfer(String accessToken, long amount) {

        return requestTransfer(accessToken, amount, null);
    }

    /**
     * Request a transfer of Quarters from a user's account
     *
     * @param accessToken User's access token
     * @param amount      Amount to transfer
     * @param description Description (optional, shows on authorization page)
     * @return Invocation of method to get {@link TransferRequest}
     */
    public Call<TransferRequest> requestTransfer(String accessToken, long amount, String description) {

        return service.requestTransfer(accessToken, TransferRequest.getRequestBody(clientID, amount,
                description));
    }

    /**
     * @return Current {@link QuartersEnvironment}, which changes the base API URL
     */
    public QuartersEnvironment getEnvironment() {

        return environment;
    }

    /**
     * @return The URL for page allowing user to authorize app to access their account
     */
    public String getAuthorizationURL() {

        return environment.getDefaultQuartersURL() + "/oauth/authorize" +
                "?response_type=code" +
                "&client_id=" + clientID +
                "&inline=true";
    }

    /**
     * @param redirectURL URL to redirect user to after they have authorized app
     * @return URL for page allowing user to authorize app to access their account
     */
    public String getAuthorizationURL(String redirectURL) throws UnsupportedEncodingException {

        return environment.getDefaultQuartersURL() + "/oauth/authorize?response_type=code&inline=true" +
                "&client_id=" + clientID +
                "&redirect_uri=" + URLEncoder.encode(redirectURL, Charset.defaultCharset().name());
    }

    /**
     * See {@link #createGuestAccount(String)}
     *
     * @param accessToken Guest user's access token
     * @return URL allowing guest user to sign up for an account
     */
    public String getGuestSignupURL(String accessToken) {

        return environment.getQuartersURL() + "/guest?response_type=code&inline=true" +
                "&client_id=" + clientID +
                "&token=" + accessToken;
    }

    /**
     * See {@link #createGuestAccount(String)}
     *
     * @param accessToken Guest user's access token
     * @param redirectURL URL to redirect user to after they have signed up
     * @return URL allowing guest user to sign up for an account
     */
    public String getGuestSignupURL(String accessToken, String redirectURL) throws UnsupportedEncodingException {

        return environment.getQuartersURL() + "/guest?response_type=code&inline=true" +
                "&client_id=" + clientID +
                "&toStr=" + accessToken +
                "&redirect_uri=" + URLEncoder.encode(redirectURL, Charset.defaultCharset().name());
    }

    /**
     * Get transfer authorization URL for user. See {@link #requestTransfer(String, long, String)}
     *
     * @param requestID ID of {@link TransferRequest}
     * @return URL allowing user to authorize transfer
     */
    public String getTransferAuthorizationURL(String requestID) {

        return environment.getQuartersURL() + "/requests/" + requestID + "?inline=true";
    }

    /**
     * Get transfer authorization URL for guest user. See {@link #requestTransfer(String, long, String)}
     *
     * @param requestID     ID of {@link TransferRequest}
     * @param firebaseToken Firebase token (for guest accounts only - see {@link #createGuestAccount(String)}
     * @return URL allowing guest user to authorize transfer
     */
    public String getTransferAuthorizationURL(String requestID, String firebaseToken) {

        return environment.getQuartersURL() + "/requests/" + requestID + "?inline=true" +
                "&firebase_token=" + firebaseToken;
    }

    /**
     * @return URL showing a user their refresh and access token
     */
    public String getTokenURL() {

        return environment.getQuartersURL() + "/access-token?app_id=" + clientID + "&app_key=" + clientKey;
    }
}