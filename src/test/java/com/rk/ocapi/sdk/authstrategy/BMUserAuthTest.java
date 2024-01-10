package com.rk.ocapi.sdk.authstrategy;

import com.rk.ocapi.sdk.utils.EnvReader;
import com.rk.ocapi.sdk.utils.JWTUtils;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.http.HttpRequest;

import static org.junit.jupiter.api.Assertions.*;

class BMUserAuthTest {
    @Test
    @DisplayName("Generate BM Auth Token. Return valid BM auth token")
    public void applyAuthentication() {
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

        // Check that returned token isn't empty
        Assertions.assertFalse(bmAuthToken.isEmpty());
        Assertions.assertFalse(JWTUtils.jwtTokenExpired(bmAuthToken));
    }

    @Test
    @DisplayName("Generate BM Auth Token. Use not valid credentials")
    public void applyAuthenticationWithNotValidCred() {
        BMUserAuth bmUserAuth = new BMUserAuth(
                EnvReader.getProperty("OCAPI_HOST"),
                EnvReader.getProperty("OCAPI_CLIENT_ID"),
                "not valid pass",
                EnvReader.getProperty("BM_USER_EMAIL"),
                EnvReader.getProperty("BM_API_KEY")
        );

        HttpRequest.Builder request = HttpRequest.newBuilder();

        Assertions.assertThrows(RuntimeException.class, () -> {
            bmUserAuth.applyAuthentication(request);
        });
    }
}