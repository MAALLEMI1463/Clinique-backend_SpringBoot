package com.example.demo.security;

import com.example.demo.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // ---- Extract username from token ----
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // ---- Extract user id from token ----
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userId", Long.class);
    }

    // ---- Extract role from token ----
    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("role", String.class);
    }

    // ---- Extract a specific claim ----
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // ---- Generate token for a User ----
    public String generateToken(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("userId", user.getId());
        extraClaims.put("role", user.getRole().name());
        return generateToken(extraClaims, user.getUsername());
    }

    // ---- Generate token with extra claims ----
    // FIXED for JJWT 0.12.x: use claims(), subject(), issuedAt(), expiration(), signWith()
    public String generateToken(Map<String, Object> extraClaims, String username) {
        return Jwts.builder()
                .claims(extraClaims)                                          // ✅ replaces setClaims()
                .subject(username)                                            // ✅ replaces setSubject()
                .issuedAt(new Date(System.currentTimeMillis()))               // ✅ replaces setIssuedAt()
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration)) // ✅ replaces setExpiration()
                .signWith(getSignInKey())                                     // ✅ no need to specify algorithm
                .compact();
    }

    // ---- Validate token ----
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // ---- Check if token is expired ----
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // ---- Extract expiration date ----
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // ---- Extract all claims ----
    // FIXED for JJWT 0.12.x: Jwts.parser() replaces Jwts.parserBuilder()
    //                          verifyWith() replaces setSigningKey()
    //                          parseSignedClaims() replaces parseClaimsJws()
    //                          .getPayload() replaces .getBody()
    private Claims extractAllClaims(String token) {
        return Jwts.parser()                         // ✅ replaces parserBuilder()
                .verifyWith(getSignInKey())           // ✅ replaces setSigningKey()
                .build()
                .parseSignedClaims(token)            // ✅ replaces parseClaimsJws()
                .getPayload();                       // ✅ replaces getBody()
    }

    // ---- Get signing key ----
    // FIXED: returns SecretKey instead of Key (required by JJWT 0.12.x verifyWith())
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}