package com.rk.ocapi.sdk.authstrategy;

import com.rk.ocapi.sdk.utils.EnvReader;
import com.rk.ocapi.sdk.utils.JWTUtils;
import com.rk.ocapi.sdk.utils.OCAPIUrlConfig;
import jdk.jfr.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.http.HttpRequest;

import static org.junit.jupiter.api.Assertions.*;

class OnBehalfOfCustomerAuthTest {
    private OnBehalfOfCustomerAuth onBehalfOfCustomerAuth;
    private OCAPIUrlConfig ocapiUrlConfig;
    private BMUserAuth bmUserAuth;

    @BeforeEach
    void setUp() {
        this.ocapiUrlConfig = new OCAPIUrlConfig(
                EnvReader.getProperty("OCAPI_HOST"),
                EnvReader.getProperty("OCAPI_VERSION"),
                EnvReader.getProperty("OCAPI_SITE_ID"),
                EnvReader.getProperty("OCAPI_CLIENT_ID"),
                EnvReader.getProperty("OCAPI_CLIENT_PASS")
        );
        this.bmUserAuth = new BMUserAuth(
                ocapiUrlConfig,
                EnvReader.getProperty("BM_USER_EMAIL"),
                EnvReader.getProperty("BM_API_KEY")
        );
        String customerId = EnvReader.getProperty("TEST_CUSTOMER_ID");

        this.onBehalfOfCustomerAuth = new OnBehalfOfCustomerAuth(
                bmUserAuth,
                ocapiUrlConfig,
                customerId
        );
    }

    @AfterEach
    void tearDown() {
        this.ocapiUrlConfig = null;
        this.bmUserAuth = null;
        this.onBehalfOfCustomerAuth = null;
    }

    @Test
    @Description("Generate new Customer On Behalf token. Validate token")
    void applyAuthentication() {
        HttpRequest.Builder request = HttpRequest.newBuilder();
        onBehalfOfCustomerAuth.applyAuthentication(request);

        String authToken = onBehalfOfCustomerAuth.getAuthToken()
                .replace("Bearer ", "");

        // Check that returned token isn't empty
        Assertions.assertFalse(authToken.isEmpty());
        Assertions.assertFalse(JWTUtils.jwtTokenExpired(authToken));
    }
}