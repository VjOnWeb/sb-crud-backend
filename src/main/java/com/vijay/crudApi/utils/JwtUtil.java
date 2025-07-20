package com.vijay.crudApi.utils;


import java.security.Key;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.vijay.crudApi.Repo.ErrorLogService;
import com.vijay.crudApi.models.AppUsers;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private final Dotenv dotenv = Dotenv.load(); // .env loader

    private final String SECRET_KEY =  dotenv.get("JWT_SECRET");

    @Autowired
    private ErrorLogService errorLogService;

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (ExpiredJwtException e) {
            logErrorToDB(e, "JwtUtil", "JWT_EXPIRED", token);
            return null;
        } catch (JwtException e) {
            logErrorToDB(e, "JwtUtil", "JWT_INVALID", token);
            return null;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        } catch (ExpiredJwtException e) {
            logErrorToDB(e, "JwtUtil", "JWT_EXPIRED", token);
            return null;
        } catch (JwtException e) {
            logErrorToDB(e, "JwtUtil", "JWT_INVALID", token);
            return null;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(AppUsers user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", user.getUsername());
        claims.put("role", user.getRole());
        return createToken(claims, user.getEmail()); // email as subject
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username != null && username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        try {
            Date expiration = extractClaim(token, Claims::getExpiration);
            return expiration.before(new Date());
        } catch (Exception e) {
            return true; // treat invalid/expired tokens as expired
        }
    }

    private void logErrorToDB(Exception e, String className, String errorCode, String affectedToken) {
        logger.warn("JWT error: {}", e.getMessage());

        Map<String, String> data = new HashMap<>();
        data.put("token", affectedToken);

        errorLogService.logError(
            new Timestamp(System.currentTimeMillis()),
            className,
            errorCode,
            e.getMessage(),
            data
        );
    }
}
