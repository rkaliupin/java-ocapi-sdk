package com.rk.ocapi.sdk.authstrategy;

import java.net.http.HttpRequest;

/**
 * Perform OCAPI Data API auth strategy
 */
public class DataApiAuth implements AuthStrategy {
    public DataApiAuth() {

    }
    /**
     * Apply auth strategy to the HttpURLConnection
     * @param requestBuilder
     */
    @Override
    public void applyAuthentication(HttpRequest.Builder requestBuilder) {
    }
}
