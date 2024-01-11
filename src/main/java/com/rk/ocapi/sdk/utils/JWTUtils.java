package com.rk.ocapi.sdk.utils;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;

/**
 * Utils for manipulations with JWT token
 */
public class JWTUtils {
    /**
     * Check whenever JWT token expired
     * @param jwt JWT token
     * @return {boolean} true in case if JWT expired
     */
    public static boolean jwtTokenExpired(String jwt) {
        if (jwt == null || jwt.isEmpty()) return true;

        jwt = jwt.contains("Bearer") ? jwt.replace("Bearer", "") : jwt;

        try {
            SignedJWT signedJWT = SignedJWT.parse(jwt);

            // Retrieve the JWT claims
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            // Get the expiration time
            java.util.Date expirationTime = claimsSet.getExpirationTime();

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
