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
    void applyAuthentication(HttpRequest.Builder requestBuilder);
}
