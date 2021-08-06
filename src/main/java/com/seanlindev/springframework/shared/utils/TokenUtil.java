package com.seanlindev.springframework.shared.utils;

import com.seanlindev.springframework.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.Date;

public class TokenUtil {
    public static boolean hasTokenExpired(String token) {
        Claims claims = Jwts.parser().setSigningKey(SecurityConstants.getTokenSecret())
                .parseClaimsJws(token).getBody();

        Date tokenExpirationDate = claims.getExpiration();
        Date todayDate = new Date();

        return tokenExpirationDate.before(todayDate);
    }
}
