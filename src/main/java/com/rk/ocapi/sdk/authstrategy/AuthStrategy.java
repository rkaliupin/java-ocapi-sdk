package com.rk.ocapi.sdk.authstrategy;

import java.net.http.HttpRequest;

/**
 * Represent auth strategy interface
 */
public interface AuthStrategy {
    /**
     * Apply auth strategy to the HttpRequest.Builder
     * @param requestBuilder
     */
    public void applyAuthentication(HttpRequest.Builder requestBuilder);

    /**
     * Return Auth token
     * @return Auth token
     */
    public String getAuthToken();
}
