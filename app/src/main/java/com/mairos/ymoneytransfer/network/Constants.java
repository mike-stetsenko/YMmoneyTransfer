package com.mairos.ymoneytransfer.network;

public final class Constants {

    public static final String API_URL = "https://money.yandex.ru";

    public static final String AUTHORIZATION_URL = "https://m.money.yandex.ru/oauth/authorize";

    public static final String CLIENT_ID = "[insert_your_client_id_here]"; // https://money.yandex.ru/myservices/new.xml

    public static final String REDIRECT_URL = "ymoneytransfer://success"; // must be equal to the one in app settings !!!

    public static final String SCOPE_P2P = "payment-p2p";

    public static final String APP_SCOPES = SCOPE_P2P;
}
