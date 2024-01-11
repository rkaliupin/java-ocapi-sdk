package com.rk.ocapi.sdk;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.rk.ocapi.sdk.authstrategy.BMUserAuth;
import com.rk.ocapi.sdk.authstrategy.OnBehalfOfCustomerAuth;
import com.rk.ocapi.sdk.utils.EnvReader;
import com.rk.ocapi.sdk.utils.OCAPIUrlConfig;

import java.text.ParseException;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        // Get the JWT parser

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

        String customerId = EnvReader.getProperty("TEST_CUSTOMER_ID");
        OnBehalfOfCustomerAuth onBehalfOfCustomerAuth = new OnBehalfOfCustomerAuth(
                bmUserAuth,
                ocapiUrlConfig,
                customerId
        );

        System.out.println(onBehalfOfCustomerAuth.getAuthToken());

        // bmUserAuth.performAuthentication();

//        String jwtToken = "";
//
//        try {
//            // Parse the JWT token
//            SignedJWT signedJWT = SignedJWT.parse(jwtToken);
//
//            // Retrieve the JWT claims
//            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
//
//            // Get the expiration time
//            java.util.Date expirationTime = claimsSet.getExpirationTime();
//            System.out.println("Expiration Time: " + expirationTime);
//
//            // Check if the token has expired
//            if (expirationTime != null && expirationTime.before(new java.util.Date())) {
//                System.out.println("Token has expired");
//            } else {
//                System.out.println("Token is still valid");
//            }
//
//            // Access other claims as needed
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
    }
}