package com.mairos.ymoneytransfer.network.requestResults;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@StringDef({ProcessPaymentResultCode.SUCCESS, ProcessPaymentResultCode.REFUSED,
        ProcessPaymentResultCode.IN_PROGRESS, ProcessPaymentResultCode.EXT_AUTH_REQUIRED})
@Retention(RetentionPolicy.SOURCE)
public @interface ProcessPaymentResultCode {
    String SUCCESS = "success";
    String REFUSED = "refused";
    String IN_PROGRESS = "in_progress";
    String EXT_AUTH_REQUIRED = "ext_auth_required";
}
