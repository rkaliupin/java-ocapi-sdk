package com.rk.ocapi.sdk.authstrategy;

import java.net.HttpURLConnection;

/**
 * Perform OCAPI Data API auth strategy
 */
public class DataApiAuth implements AuthStrategy {
    public DataApiAuth() {

    }
    /**
     * Apply auth strategy to the HttpURLConnection
     * @param connection
     */
    @Override
    public void applyAuthentication(HttpURLConnection connection) {

    }
}
