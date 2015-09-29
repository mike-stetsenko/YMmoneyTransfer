package com.mairos.ymoneytransfer.network.requests;

import com.mairos.ymoneytransfer.network.Constants;
import com.mairos.ymoneytransfer.network.YMoneyApi;
import com.mairos.ymoneytransfer.network.events.ProcessPaymentEvent;
import com.mairos.ymoneytransfer.network.events.RequestPaymentEvent;
import com.mairos.ymoneytransfer.network.requestResults.ProcessPaymentResult;
import com.mairos.ymoneytransfer.network.requestResults.ProcessPaymentResultCode;
import com.mairos.ymoneytransfer.network.requestResults.RequestPaymentResult;
import com.octo.android.robospice.request.SpiceRequest;

import org.androidannotations.annotations.Background;

import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;

public class ProcessPaymentRequest extends SpiceRequest<Void> {

    private String mToken;
    private String mRequestId;
    private String mProtectionCode;

    public ProcessPaymentRequest(String token, String requestId, String protectionCode) {
        super(Void.class);
        mToken = token;
        mRequestId = requestId;
        mProtectionCode = protectionCode;
    }

    @Override
    public Void loadDataFromNetwork() throws Exception {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Constants.API_URL)
                .build();

        YMoneyApi retrofitService = restAdapter.create(YMoneyApi.class);

        ProcessPaymentResult result = retrofitService.processPayment("Bearer " + mToken,
                mRequestId, "wallet");

        EventBus.getDefault().postSticky(new ProcessPaymentEvent(result, mProtectionCode));

        return null;
    }
}
