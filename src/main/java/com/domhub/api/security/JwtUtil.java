package com.domhub.api.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    // Thời gian token có hiệu lực (1 giờ)
    private static final long JWT_TOKEN_VALIDITY = 100L * 365 * 24 * 60 * 60 * 1000; // 100 năm


    private final String secretString;

    public JwtUtil(String secretString) {
        this.secretString = secretString;
    }

    public JwtUtil() {
        this.secretString = "defaultSecretKeyPleaseChangeInProductionEnvironment";
    }

    // Lấy signing key từ chuỗi bí mật
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    }

    // Trích xuất username từ token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Trích xuất ID từ token
    public Integer extractAccountId(String token) {
        return extractClaim(token, claims -> claims.get("id", Integer.class));
    }


    public Integer extractAccountIdFromHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        String token = authHeader.substring(7);
        return extractAccountId(token);
    }


    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    // Trích xuất thời gian hết hạn từ token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Trích xuất một claim cụ thể từ token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Giải mã token và lấy toàn bộ claims
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Kiểm tra token đã hết hạn chưa
    public Boolean isTokenExpired(String token) {
        final Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }

    // Tạo token với các claims bổ sung
    public String generateToken(String username, Map<String, Object> additionalClaims) {
        Map<String, Object> claims = new HashMap<>(additionalClaims);
        return createToken(claims, username);
    }

    // Tạo JWT token
    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_TOKEN_VALIDITY);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    // Xác thực token có hợp lệ không
    public Boolean validateToken(String token, String username) {
        try {
            final String extractedUsername = extractUsername(token);
            return (extractedUsername.equals(username) && !isTokenExpired(token));
        } catch (SignatureException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // Kiểm tra token có hợp lệ về mặt cú pháp không
    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Tính toán thời gian còn lại của token (milliseconds)
    public long getTokenRemainingTimeInMillis(String token) {
        Date expiration = extractExpiration(token);
        Date now = new Date();
        return expiration.getTime() - now.getTime();
    }

    // Trích xuất một claim cụ thể theo tên
    public Object getClaimFromToken(String token, String claimName) {
        Claims claims = extractAllClaims(token);
        return claims.get(claimName);
    }

    // Tạo token với thời gian hết hạn tùy chỉnh
    public String generateTokenWithCustomExpiration(String username, long expirationTimeInMillis) {
        Map<String, Object> claims = new HashMap<>();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTimeInMillis);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }





    public String extractRoleFromHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        String token = authHeader.substring(7);
        return extractRole(token);
    }
}
