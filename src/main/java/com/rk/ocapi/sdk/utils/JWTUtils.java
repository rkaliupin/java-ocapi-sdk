package com.rk.ocapi.sdk.utils;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;
public class JWTUtils {
    public static boolean jwtTokenExpired(String jwt) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(jwt);

            // Retrieve the JWT claims
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            // Get the expiration time
            java.util.Date expirationTime = claimsSet.getExpirationTime();
            System.out.println("Expiration Time: " + expirationTime);

            // Check if the token has expired
            if (expirationTime != null && expirationTime.before(new java.util.Date())) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return true;
    }
}
