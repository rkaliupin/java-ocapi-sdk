package com.rk.ocapi.sdk.authstrategy;

import java.net.HttpURLConnection;

public class BMUserAuth implements AuthStrategy {
    private final String host;
    private final String clientId;
    private final String authHeader;
    private String bmAuthToken = "";

    public BMUserAuth(String host, String clientId, String clientTokenPass, String bmUserEmail, String bmApiKey) {
        this.host = host;
        this.clientId = clientId;
        this.authHeader = java.util.Base64.getEncoder().encodeToString(
                (bmUserEmail + ":" + bmApiKey + ":" + clientTokenPass).getBytes()
        );
    }

    /**
     * Apply auth strategy to the HttpURLConnection
     * @param connection
     */
    @Override
    public void applyAuthentication(HttpURLConnection connection) {

    }

    private String performAuthentication() {
        return "";
    }

    private Boolean validateAuthToken(String authToken) {
        if (bmAuthToken.isEmpty()) return false;

        // Decode token
        return true;
    }
}
