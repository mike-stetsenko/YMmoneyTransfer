package com.mairos.ymoneytransfer.network.requests;

import com.mairos.ymoneytransfer.network.Constants;
import com.mairos.ymoneytransfer.network.YMoneyApi;
import com.mairos.ymoneytransfer.network.events.RequestPaymentEvent;
import com.mairos.ymoneytransfer.network.requestResults.RequestPaymentResult;
import com.mairos.ymoneytransfer.network.requestResults.RequestPaymentResultCode;
import com.octo.android.robospice.request.SpiceRequest;

import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;
import retrofit.RetrofitError;

public class RequestPaymentRequest extends SpiceRequest<Void> {

    private String mToken;
    private String mId;
    private String mAmountDue;
    private String mComment;
    private int mExpirePeriod;


    public RequestPaymentRequest(String token, String id, String amountDue, String comment, int expirePeriod) {
        super(Void.class);
        mToken = token;
        mId = id;
        mAmountDue = amountDue;
        mComment = comment;
        mExpirePeriod = expirePeriod;
    }

    @Override
    public Void loadDataFromNetwork() throws Exception {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.API_URL)
                .build();

        YMoneyApi retrofitService = restAdapter.create(YMoneyApi.class);

        RequestPaymentResult result;
        try {
            result = retrofitService.requestPayment("Bearer " + mToken,
                    "p2p", mId, mAmountDue, mComment, true, mExpirePeriod);
        } catch (RetrofitError re){
            result = new RequestPaymentResult();
            result.setStatus(RequestPaymentResultCode.REFUSED);
            result.setError("auth error");
            result.setErrorDescription("check pincode");
        }

        EventBus.getDefault().postSticky(new RequestPaymentEvent(result, mToken));

        return null;
    }
}
