package com.rk.ocapi.sdk.authstrategy;

import com.rk.ocapi.sdk.utils.EnvReader;
import com.rk.ocapi.sdk.utils.JWTUtils;
import com.rk.ocapi.sdk.utils.OCAPIUrlConfig;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.http.HttpRequest;

import static org.junit.jupiter.api.Assertions.*;

class BMUserAuthTest {
    @Test
    @DisplayName("Generate BM Auth Token. Return valid BM auth token")
    public void applyAuthentication() {
        OCAPIUrlConfig ocapiUrlConfig = new OCAPIUrlConfig(
                EnvReader.getProperty("OCAPI_HOST"),
                EnvReader.getProperty("OCAPI_VERSION"),
                EnvReader.getProperty("OCAPI_SITE_ID"),
                EnvReader.getProperty("OCAPI_CLIENT_ID"),
                EnvReader.getProperty("OCAPI_CLIENT_PASS")
        );
        BMUserAuth bmUserAuth = new BMUserAuth(
                ocapiUrlConfig,
                EnvReader.getProperty("BM_USER_EMAIL"),
                EnvReader.getProperty("BM_API_KEY")
        );

        HttpRequest.Builder request = HttpRequest.newBuilder();
        bmUserAuth.applyAuthentication(request);

        String bmAuthToken = bmUserAuth.getAuthToken()
                .replace("Bearer ", "");

        // Check that returned token isn't empty
        Assertions.assertFalse(bmAuthToken.isEmpty());
        Assertions.assertFalse(JWTUtils.jwtTokenExpired(bmAuthToken));
    }

    @Test
    @DisplayName("Generate BM Auth Token. Use not valid credentials")
    public void applyAuthenticationWithNotValidCred() {
        OCAPIUrlConfig ocapiUrlConfig = new OCAPIUrlConfig(
                EnvReader.getProperty("OCAPI_HOST"),
                EnvReader.getProperty("OCAPI_VERSION"),
                EnvReader.getProperty("OCAPI_SITE_ID"),
                EnvReader.getProperty("OCAPI_CLIENT_ID"),
                "not valid pass"
        );
        BMUserAuth bmUserAuth = new BMUserAuth(
                ocapiUrlConfig,
                EnvReader.getProperty("BM_USER_EMAIL"),
                EnvReader.getProperty("BM_API_KEY")
        );

        HttpRequest.Builder request = HttpRequest.newBuilder();

        Assertions.assertThrows(RuntimeException.class, () -> {
            bmUserAuth.applyAuthentication(request);
        });
    }
}