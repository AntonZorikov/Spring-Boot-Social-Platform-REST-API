package com.example.jeddit.servicies.impl;


import com.example.jeddit.models.entitys.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService {

    //private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final String SECRET_KEY = "KnnJBkjUjbDiKDmRLdOdkKnnJBkjUjbDiKDmRLdOdkNNnMwIrJdSIoJSKjKJISoLWdmMfBdfGgGYEnLklFsGkoOJNNnMwIrJdSIoJSKjKJISoLWdmMfBdfGgGYEnLklFsGkoOJ";
    private static final long EXPIRATION_TIME = 3600000;

    public static String generateToken(User user) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME);

        Map<String, Object> claims = new HashMap<>();
        claims.put("userid", user.getId());
        claims.put("role", user.getRole());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public long extractUserId(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().get("userid", Long.class);
    }

}
