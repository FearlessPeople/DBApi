package com.dbapi.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class JwtUtil {

    @Value("${jwt.secret}") // 从配置文件中获取密钥
    private String secret;

    @Value("${jwt.expiration}") // 从配置文件中获取 token 过期时间
    private long expiration;

    public JwtUtil() {
    }

    /**
     * 生成 token
     *
     * @param userId
     * @return
     */
    public String generateToken(String userId) {
        Date expiryDate = new Date(System.currentTimeMillis() + expiration);

        // jwt里的用户信息
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);

        // 生成JWT token
        String token = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setClaims(claims)
                .setExpiration(expiryDate)
                .setId(UUID.randomUUID().toString())
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();

        return token;
    }

    /**
     * 验证 token
     *
     * @param token
     * @return
     */
    public boolean checkToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 解析 token 并获取 Claims 对象
     *
     * @param token JWT token
     * @return Claims 对象
     */
    public Claims parseToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            // 处理异常，例如打印日志或返回null
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从Token中获取userId
     *
     * @param token
     * @return
     */
    public Integer getUserId(String token) {
        Claims claims = this.parseToken(token);
        if (claims != null) {
            String userIdStr = (String) claims.get("userId");
            return userIdStr != null ? Integer.valueOf(userIdStr) : null;
        }
        return null;
    }

    /**
     * 生成加密密钥
     *
     * @return
     */
    private Key getKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public static void main(String[] args) {

        // SecureRandom secureRandom = new SecureRandom();
        // byte[] key = new byte[32]; // 256 bits
        // secureRandom.nextBytes(key);
        // String base64Key = Base64.getEncoder().encodeToString(key);
        // System.out.println("Generated secret key: " + base64Key);

        JwtUtil jwtUtil = new JwtUtil();
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VySWQiOiIwIiwiZXhwIjoxNzI1NDM4NDgzLCJqdGkiOiIwODliZjk3NC1kYzA0LTQ4MTctYjE5Mi00ZGU4YTBjYjU1YjQifQ.c6X1cPCgudnsM1OSbIRAK6DP4rf7uyESr2LFChcDXIU";
        // String token = "";
        boolean b = jwtUtil.checkToken(token);
        System.out.println(b);
        System.out.println(jwtUtil.parseToken(token).getExpiration());
    }
}
