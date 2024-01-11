package com.rk.ocapi.sdk.authstrategy;

import com.rk.ocapi.sdk.utils.JWTUtils;
import com.rk.ocapi.sdk.utils.OCAPIUrlConfig;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class OnBehalfOfCustomerAuth implements AuthStrategy {
    private OOCAuthStrategy oocAuthStrategy;
    private String customerId;
    private OCAPIUrlConfig ocapiUrlConfig;
    private String authToken;

    public OnBehalfOfCustomerAuth(OOCAuthStrategy oocAuthStrategy, OCAPIUrlConfig ocapiUrlConfig, String customerId) {
        this.oocAuthStrategy = oocAuthStrategy;
        this.ocapiUrlConfig = ocapiUrlConfig;
        this.customerId = customerId;
    }

    /**
     * Apply auth strategy to the HttpRequest.Builder
     *
     * @param requestBuilder
     */
    @Override
    public void applyAuthentication(HttpRequest.Builder requestBuilder) {
        this.performAuthentication();
        requestBuilder.header("Authorization", this.authToken);
    }

    @Override
    public String getAuthToken() {
        return this.authToken;
    }

    private void performAuthentication() {
        // If token didn't expire -> return the token
        if (!JWTUtils.jwtTokenExpired(this.authToken)) return;

        // Define URL
        String url = ocapiUrlConfig.getShopBaseUrl() + "/customers/" + this.customerId + "/auth";

        try {
            // Make a request
            HttpClient httpClient = HttpClient.newBuilder().build();
            HttpRequest.Builder builder = HttpRequest.newBuilder()
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .uri(URI.create(url));

            this.oocAuthStrategy.applyAuthentication(builder);

            HttpRequest request = builder.build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            //

            if (response.statusCode() != 200) {
                throw new RuntimeException("Bad Request: " + responseBody);
            }

            // Get Auth token from the header
            this.authToken = response.headers().firstValue("Authorization").get();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
