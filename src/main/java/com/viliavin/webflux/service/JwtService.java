package com.viliavin.webflux.service;

import com.viliavin.webflux.domain.model.AuthTokens;
import com.viliavin.webflux.property.JwtProperties;
import com.viliavin.webflux.property.Token;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JwtService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public AuthTokens generateTokens(User user) {
        String access = generateAccessToken(user);
        String refresh = generateRefreshToken(user);

        return new AuthTokens(access, refresh);
    }

    public String generateAccessToken(User user) {
        Token access = this.jwtProperties.tokens().access();

        return Jwts.builder()
            .setClaims(Map.of("ROLE", user.getAuthorities().stream().map(Object::toString)
                .collect(Collectors.joining(", "))))
            .setSubject(user.getUsername())
            .setIssuer(this.jwtProperties.issuer())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + access.ttlInSeconds() * 1000))
            .signWith(Keys.hmacShaKeyFor(access.key().getBytes()), SignatureAlgorithm.HS512)
            .compact();
    }

    public String generateRefreshToken(User user) {
        Token refresh = this.jwtProperties.tokens().refresh();

        return Jwts.builder()
            .setSubject(user.getUsername())
            .setIssuer(this.jwtProperties.issuer())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + refresh.ttlInSeconds() * 1000))
            .signWith(Keys.hmacShaKeyFor(refresh.key().getBytes()), SignatureAlgorithm.HS512)
            .compact();
    }

    public boolean validateAccessToken(String token) {
        Token access = this.jwtProperties.tokens().access();

        try {
            Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(access.key().getBytes()))
                .build()
                .parseClaimsJws(token);

            return true;
        } catch (ExpiredJwtException ex) {
            logger.error("JWT expired", ex);
        } catch (IllegalArgumentException ex) {
            logger.error("Token is null, empty or only whitespace", ex);
        } catch (MalformedJwtException ex) {
            logger.error("JWT is invalid", ex);
        } catch (UnsupportedJwtException ex) {
            logger.error("JWT is not supported", ex);
        } catch (SignatureException ex) {
            logger.error("Signature validation failed");
        }

        return false;
    }

    public Claims parseAccessClaims(String token) {
        Token access = this.jwtProperties.tokens().access();

        return Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(access.key().getBytes()))
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

}
