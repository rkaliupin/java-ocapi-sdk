package com.rk.ocapi.sdk.authstrategy;

import java.net.HttpURLConnection;

/**
 * Represent auth strategy interface
 */
public interface AuthStrategy {
    /**
     * Apply auth strategy to the HttpURLConnection
     * @param {HttpURLConnection} connection
     */
    void applyAuthentication(HttpURLConnection connection);
}
