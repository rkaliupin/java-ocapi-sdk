package com.rk.ocapi.sdk.authstrategy;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rk.ocapi.sdk.utils.JWTUtils;

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

public class BMUserAuth implements AuthStrategy {
    private final String host;
    private final String clientId;
    private final String authHeader;
    private String bmAuthToken = "";

    public BMUserAuth(String host, String clientId, String clientTokenPass, String bmUserEmail, String bmApiKey) {
        this.host = host;
        this.clientId = clientId;
        this.authHeader = "Basic " + java.util.Base64.getEncoder().encodeToString(
                (bmUserEmail + ":" + bmApiKey + ":" + clientTokenPass).getBytes()
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
    public String getBmAuthToken() {
        return this.bmAuthToken;
    }

    /**
     * Apply Auth Strategy to the request (request builder)
     */
    private void performAuthentication() {
        // If token didn't expire -> return the token
        if (this.authTokenValid(this.bmAuthToken)) return;

        // Define URL
        String url = "https://" + this.host + "/dw/oauth2/access_token?client_id=" + this.clientId;

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
            String accessToken = jsonObject.get("access_token").toString();
            //

            this.bmAuthToken = "Bearer " + accessToken;
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Validate auth token
     * @param authToken auth token to validate (static method potentially can be moved to the separate class)
     * @return {boolean} true/false in case if the auth token is valid or not
     */
    private Boolean authTokenValid(String authToken) {
        if (authToken.isEmpty()) return false;
        if (JWTUtils.jwtTokenExpired(authToken)) return false;

        return true;
    }
}
