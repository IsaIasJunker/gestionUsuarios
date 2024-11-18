package com.grupo1.demo.Jwt;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    // Genera una clave aleatoria para la firma del token
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long expirationTimeMs = 1000 * 60 * 60 * 24; // 1 dia de expiración

    /**
     * Genera un token con los datos del usuario.
     */
    public String getToken(UserDetails user) {
        return generateToken(new HashMap<>(), user.getUsername());
    }

    /**
     * Genera un token con claims personalizados y el username.
     */
    public String generateToken(Map<String, Object> extraClaims, String username) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeMs))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Obtiene el username almacenado en el token.
     */
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    /**
     * Verifica si el token es válido para un usuario en particular.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Extrae un claim específico del token.
     */
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Valida si el token ha expirado.
     */
    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    /**
     * Obtiene la fecha de expiración del token.
     */
    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    /**
     * Extrae todos los claims del token.
     */
    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}