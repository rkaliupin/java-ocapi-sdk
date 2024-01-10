package com.rk.ocapi.sdk.utils;

import com.rk.ocapi.sdk.authstrategy.BMUserAuth;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.net.http.HttpRequest;

import static org.junit.jupiter.api.Assertions.*;

class JWTUtilsTest {

    @Test
    @DisplayName("Not expired JWT token")
    public void jwtTokenNotExpired() {
        // Generate JWT token
        BMUserAuth bmUserAuth = new BMUserAuth(
                EnvReader.getProperty("OCAPI_HOST"),
                EnvReader.getProperty("OCAPI_CLIENT_ID"),
                EnvReader.getProperty("OCAPI_CLIENT_PASS"),
                EnvReader.getProperty("BM_USER_EMAIL"),
                EnvReader.getProperty("BM_API_KEY")
        );

        HttpRequest.Builder request = HttpRequest.newBuilder();
        bmUserAuth.applyAuthentication(request);

        String bmAuthToken = bmUserAuth.getBmAuthToken()
                .replace("Bearer ", "");
        //

        Assertions.assertFalse(JWTUtils.jwtTokenExpired(bmAuthToken));
    }

    @Test
    @DisplayName("Expired JWT token")
    public void jwtTokenExpired() {
        // Expired JWT token
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJJc3N1ZXIiLCJVc2VybmFtZSI6IkphdmFJblVzZSIsImV4cCI6MTY3MzMyNDk5NCwiaWF0IjoxNzA0ODYwOTk0fQ.RA7FQcNtimMzLh3Cnu0QG06gPJMCqVw-Juy4eZeOZJk";

        Assertions.assertTrue(JWTUtils.jwtTokenExpired(jwt));
    }
}