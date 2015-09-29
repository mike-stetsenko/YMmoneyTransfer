package com.mairos.ymoneytransfer.network;

import com.mairos.ymoneytransfer.network.requestResults.AccessTokenResult;
import com.mairos.ymoneytransfer.network.requestResults.ProcessPaymentResult;
import com.mairos.ymoneytransfer.network.requestResults.RequestPaymentResult;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Header;
import retrofit.http.POST;

public interface YMoneyApi {

    @FormUrlEncoded
    @POST("/oauth/token")
    AccessTokenResult getToken(@Field("code") String code,
                               @Field("client_id") String clientId,
                               @Field("grant_type") String grantType,
                               @Field("redirect_uri") String redirectUri);

    @FormUrlEncoded
    @POST("/api/request-payment")
    RequestPaymentResult requestPayment(@Header("Authorization") String authorization,
                                        @Field("pattern_id") String patternId,
                                        @Field("to") String to,
                                        @Field("amount_due") String amountDue,
                                        @Field("comment") String comment,
                                        @Field("codepro") boolean codepro,
                                        @Field("expire_period") int expirePeriod);

    @FormUrlEncoded
    @POST("/api/process-payment")
    ProcessPaymentResult processPayment(@Header("Authorization") String authorization,
                                        @Field("request_id") String requestId,
                                        @Field("money_source") String moneySource);
}
