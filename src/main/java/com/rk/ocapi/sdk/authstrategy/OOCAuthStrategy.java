package com.rk.ocapi.sdk.authstrategy;

import java.net.http.HttpRequest;

public interface OOCAuthStrategy {
    /**
     * Apply auth strategy to the HttpRequest.Builder
     * @param requestBuilder
     */
    void applyAuthentication(HttpRequest.Builder requestBuilder);
    /**
     * Return Auth token
     * @return Auth token
     */
    public String getAuthToken();
}
