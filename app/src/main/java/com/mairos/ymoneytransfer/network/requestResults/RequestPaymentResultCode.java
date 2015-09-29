package com.mairos.ymoneytransfer.network.requestResults;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({RequestPaymentResultCode.SUCCESS, RequestPaymentResultCode.REFUSED,
        RequestPaymentResultCode.HOLD_FOR_PICKUP})
@Retention(RetentionPolicy.SOURCE)
public @interface RequestPaymentResultCode {
    String SUCCESS = "success";
    String REFUSED = "refused";
    String HOLD_FOR_PICKUP = "hold_for_pickup";
}
