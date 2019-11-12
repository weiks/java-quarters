package com.p3pi27.quarters;

import com.p3pi27.quarters.response.oauth.AccessToken;
import com.p3pi27.quarters.response.oauth.RefreshToken;
import com.p3pi27.quarters.response.transfer.TransferRequest;
import com.p3pi27.quarters.response.user.Account;
import com.p3pi27.quarters.response.user.AccountBalance;
import com.p3pi27.quarters.response.user.GuestAccount;
import com.p3pi27.quarters.response.user.User;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.Map;

public interface QuartersService {

    @POST("oauth/token")
    Call<RefreshToken> getRefreshToken(@Body Map<String, String> body);

    @POST("oauth/token")
    Call<AccessToken> getAccessToken(@Body Map<String, String> body);

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
    Call<TransferRequest> requestTransfer(@Header("Authorization") String authorization,
                                          @Body Map<String, Object> body);
}