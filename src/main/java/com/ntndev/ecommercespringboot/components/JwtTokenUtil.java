package com.ntndev.ecommercespringboot.components;

import com.ntndev.ecommercespringboot.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    @Value("${jwt.expiration}")
    private int expiration;

    @Value("${jwt.secretKey}")
    private String secretKey;

    // Tạo token JWT cho người dùng
    public String generateToken(User user) throws Exception {
        Map<String, Object> claims = new HashMap<>();
        claims.put("phoneNumber", user.getPhoneNumber());
        try {
            // Xây dựng token JWT với thông tin yêu cầu và thời hạn
            String token = Jwts.builder()
                    .setClaims(claims)
                    .setSubject(user.getPhoneNumber())
                    .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000L))
                    .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                    .compact();
            return token;
        } catch (Exception e) {
            throw new InvalidParameterException("Cannot generate token, error: " + e.getMessage());
        }
    }

    // Lấy khóa ký JWT từ secretKey
    private Key getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(bytes);
    }

    // Tạo secretKey ngẫu nhiên (nếu cần)
    private String generateSecretKey() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String secretKey = Encoders.BASE64.encode(bytes);
        return secretKey;
    }

    // Trích xuất tất cả các claims từ token
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Trích xuất một claim cụ thể từ token
    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = this.extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    // Kiểm tra token có hết hạn hay không
    public boolean isTokenExpired(String token) {
        Date expiration = this.extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    // Trích xuất số điện thoại từ token
    public String extractPhoneNumber(String token) {
        return this.extractClaim(token, Claims::getSubject);
    }

    // Xác thực token và kiểm tra tính hợp lệ
    public boolean validateToken(String token, UserDetails userDetails) {
        final String phoneNumber = this.extractPhoneNumber(token);
        return phoneNumber.equals(userDetails.getUsername()) && !this.isTokenExpired(token);
    }
}
