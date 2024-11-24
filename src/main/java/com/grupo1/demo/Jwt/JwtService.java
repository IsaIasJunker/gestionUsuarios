package com.grupo1.demo.Jwt;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.grupo1.demo.Models.Token;
import com.grupo1.demo.Models.Usuario;
import com.grupo1.demo.Repositories.TokenRepository;
import com.grupo1.demo.Repositories.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    // Genera una clave aleatoria para la firma del token
    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long expirationTimeMs = 1000 * 60 * 60 * 24; // 1 dia de expiración

    
    /**
     * Genera un token y lo asocia a un usuario.
     */
    public String generateAndStoreToken(Usuario usuario) {
        // Si ya existe un token para este usuario, eliminarlo
        Token existingToken = tokenRepository.findByUser_username(usuario.getUsername());
        if (existingToken != null) {
            tokenRepository.delete(existingToken);
        }

        // Generar el nuevo token
        String tokenValue = generateToken(new HashMap<>(), usuario.getUsername());

        // Crear y persistir el token en la base de datos
        Token token = new Token();
        token.setToken(tokenValue);
        token.setExpiresAt(new Date(System.currentTimeMillis() + expirationTimeMs));
        token.setUser(usuario);
        tokenRepository.save(token);

        return tokenValue;
    }

    /**
     * Genera un token JWT con claims personalizados.
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
     * Verifica si el token es válido para un usuario específico.
     */
    public boolean isTokenValid(String token, Usuario usuario) {
        final String username = getUsernameFromToken(token);
        Token storedToken = tokenRepository.findByUser_username(usuario.getUsername());

        // Verificar que el token sea válido y no haya expirado
        return storedToken != null &&
               storedToken.getToken().equals(token) &&
               username.equals(usuario.getUsername()) &&
               !isTokenExpired(token);
    }

    /**
     * Valida si el token ha expirado.
     */
    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    /**
     * Extrae un claim específico del token.
     */
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Obtiene la fecha de expiración del token.
     */
    public Date getExpiration(String token) {
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

    /**
     * Metodo para verificar si el header de autorizacion contiene un token valido
     * y si pertenece a un usuario existente de la base de datos
     * @param authHeader es el header de autorizacion que debe incluir el token en el formato "Bearer {token}" 
     * @return "True" si el header contiene un token válido y el usario asociado existe, de lo contrario "False".
     */
    public boolean isAuthenticationValid(String authHeader){
        //Verifica que el header no sea nulo y tenga un formato válido
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }

        //Extrae el token
        String token = authHeader.substring(7);

        //Obtiene el nombre de usuario del token
        String userName = getUsernameFromToken(token);
        //Obtiene el usuario de la base de datos con el nombre que obtuvo del token
        Usuario usuario = userRepository.findByUsername(userName);
        /*
         * Valida el token del usuario
         * Verifica que el usuario existe y luego que el token es valido para ese usuario
         */
        return usuario != null && isTokenValid(token, usuario); 
    }
}