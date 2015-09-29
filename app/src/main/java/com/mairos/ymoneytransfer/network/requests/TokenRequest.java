package com.mairos.ymoneytransfer.network.requests;

import com.mairos.ymoneytransfer.R;
import com.mairos.ymoneytransfer.YmoneyApplication;
import com.mairos.ymoneytransfer.network.Constants;
import com.mairos.ymoneytransfer.network.events.TokenEvent;
import com.mairos.ymoneytransfer.network.YMoneyApi;
import com.mairos.ymoneytransfer.network.requestResults.AccessTokenResult;
import com.octo.android.robospice.request.SpiceRequest;

import de.greenrobot.event.EventBus;
import retrofit.RestAdapter;

public class TokenRequest extends SpiceRequest<Void> {

    private String mTmpToken;

    public TokenRequest(String tmpToken) {
        super(Void.class);
        mTmpToken = tmpToken;
    }

    @Override
    public Void loadDataFromNetwork() throws Exception {

            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(Constants.API_URL)
                    .build();

            YMoneyApi retrofitService = restAdapter.create(YMoneyApi.class);

            AccessTokenResult token = retrofitService.getToken(
                    mTmpToken, Constants.CLIENT_ID, "authorization_code", Constants.REDIRECT_URL);

            if (token.getAccessToken() != "") {

                EventBus.getDefault().postSticky(new TokenEvent(true,
                        YmoneyApplication.getInstance().getString(R.string.message_token_loaded), token.getAccessToken()));
            } else {
                EventBus.getDefault().postSticky(new TokenEvent(false,
                        token.getError(), ""));
            }

            return null;
    }
}
