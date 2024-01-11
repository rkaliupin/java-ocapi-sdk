package com.rk.ocapi.sdk.authstrategy;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rk.ocapi.sdk.utils.JWTUtils;
import com.rk.ocapi.sdk.utils.OCAPIUrlConfig;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class BMUserAuth implements AuthStrategy, OOCAuthStrategy {
    private final OCAPIUrlConfig ocapiUrlConfig;
    private final String authHeader;
    private String bmAuthToken = "";

    public BMUserAuth(OCAPIUrlConfig ocapiUrlConfig, String bmUserEmail, String bmApiKey) {
        this.ocapiUrlConfig = ocapiUrlConfig;
        this.authHeader = "Basic " + java.util.Base64.getEncoder().encodeToString(
                (bmUserEmail + ":" + bmApiKey + ":" + ocapiUrlConfig.getClientPass()).getBytes()
        );
    }

    /**
     * Apply auth strategy to the HttpURLConnection
     * @param requestBuilder
     */
    @Override
    public void applyAuthentication(HttpRequest.Builder requestBuilder) {
        // Perform Authentification as a BM User
        this.performAuthentication();

        // Add auth header
        requestBuilder.header("Authorization", this.bmAuthToken);
    }

    /**
     * Get value of the bmAuthToken attr
     * @return {String} bmAuthToken
     */
    @Override
    public String getAuthToken() {
        return this.bmAuthToken;
    }

    /**
     * Apply Auth Strategy to the request (request builder)
     */
    private void performAuthentication() {
        // If token didn't expire -> return the token
        if (!JWTUtils.jwtTokenExpired(this.bmAuthToken)) return;

        // Define URL
        String url = "https://" + ocapiUrlConfig.getHost() + "/dw/oauth2/access_token?client_id=" + ocapiUrlConfig.getClientId();

        // Build request body
        Map<String, String> jsonMap = new HashMap<>();
        jsonMap.put("grant_type", "urn:demandware:params:oauth:grant-type:client-id:dwsid:dwsecuretoken");

        String body = jsonMap.entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
        //

        try {
            HttpClient httpClient = HttpClient.newBuilder().build();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", this.authHeader)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();

            if (response.statusCode() != 200) {
                throw new RuntimeException("Bad Request: " + responseBody);
            }

            // Parse response body
            JsonElement jsonElement = JsonParser.parseString(responseBody);
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String accessToken = jsonObject.get("access_token").getAsString();
            //

            this.bmAuthToken = "Bearer " + accessToken;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
