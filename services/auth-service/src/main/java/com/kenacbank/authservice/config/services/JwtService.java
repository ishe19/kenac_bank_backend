package com.kenacbank.authservice.config.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service class for handling JWT operations such as token generation, validation,
 * and claim extraction. Utilizes application-specific configurations for secret
 * keys and expiration times. Provides methods to generate access and refresh tokens,
 * validate tokens, and extract claims from tokens.
 */
@Service
public class JwtService {
    @Value("${application.security.jwt.secret-key}")
    public String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;


    private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractExpiredUsername(String token) {
        return extractExpiredClaim(token, Claims::getSubject);
    }



    public String generateToken(
            UserDetails userDetails, Map<String, Object> extraClaims) {
        return buildToken(userDetails, jwtExpiration, extraClaims);
    }

    public String generateRefreshToken(
            UserDetails userDetails) {
        return buildToken( userDetails, refreshExpiration, new HashMap<>());
    }

    private String buildToken(UserDetails userDetails, long expiration, Map<String, Object> extraClaims) {
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claims(extraClaims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey())
                .compact();
    }


    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isExpiredTokenValid(String token, UserDetails userDetails) {
        final String username = extractExpiredUsername(token);
        return (username.equals(userDetails.getUsername()));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public <T> T extractExpiredClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllExpiredClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private Claims extractAllExpiredClaims(String token) {
        try {
            return Jwts
                    .parser()
                    .verifyWith(getSignInKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
