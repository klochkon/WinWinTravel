package org.example.servicea.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    private final Key secretJwtKey;

    public JwtService(@Value("${security.secret.jwt.key}")String secretJwtKey) {
        byte[] keyBytes = Base64.getDecoder().decode(secretJwtKey);
        this.secretJwtKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateJWTToken(String email) {

        return Jwts.builder()
                .setSubject(email)
                .signWith(secretJwtKey)
                .setExpiration(Date.from(Instant.now().plusSeconds(3600*24)))
                .compact();
    }



    public boolean validateToken(String token) {
         if (isTokenExpired(token)) {
            return false;
         }

         try {
             extractAllClaims(token);
         }

         catch (Exception e) {
                return false;
         }
         return true;
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretJwtKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Date getExpirationDateFromToken(String token) {
        return extractAllClaims(token).getExpiration();
    }

    private boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(Date.from(Instant.now()));
    }
}
