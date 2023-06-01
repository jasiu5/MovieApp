package com.example.UserAuthService.Security.JWT;

import com.example.UserAuthService.Service.UserDetService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import io.jsonwebtoken.io.Decoders;


import java.security.Key;
import java.util.Date;

@Service
public class TokenProvider {

    private UserDetService userDetService;

    private String SECRET_KEY =
            "666JmRo6x36dICgULmFPreb9T4gx8hXizyHgS9LsK+wVK+jQKc5wMP/rf6fZNgn8/koyEkNPUxYPdc4vzLkT5VK2z7BnIQ/6oBs7RJyz6TPgJwAlc6runQ==";

    private int EXPIRE_DATE = 100 * 60 * 60 * 24;

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {

        Jws<Claims> claims =  Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token);
        return (!claims.getBody().getExpiration().before(new Date()));


    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        String userEmail =
                Jwts.parserBuilder()
                        .setSigningKey(getKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject();
        UserDetails userDetails = this.userDetService.loadUserByUsername(userEmail);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();

        Date issued = new Date(System.currentTimeMillis());
        Date expire = new Date(System.currentTimeMillis() + EXPIRE_DATE);

        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(issued)
                .setExpiration(expire)
                .signWith(getKey(), SignatureAlgorithm.HS512)
                .compact();

        return token;
    }

    public String getUsername(String token){
        String cl = Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token).getBody().getSubject();
        return cl;
    }


}
