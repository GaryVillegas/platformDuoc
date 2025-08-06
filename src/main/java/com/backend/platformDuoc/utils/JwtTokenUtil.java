package com.backend.platformDuoc.utils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenUtil {

    //Convertir 3 horas en segundos
    // Access token válido por 3 horas
    private static final long JWT_TOKEN_VALIDITY = 3 * 60 * 60;

    
    //método de la clase io.jsonwebtoken.security.Keys
    //que genera una clave segura (javax.crypto.SecretKey) adecuada para el algoritmo que se le indique.
    Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    //SignatureAlgorithm.HS256:
    //Es uno de los algoritmos de firma JWT. Significa "HMAC usando SHA-256".
    //Es un algoritmo simétrico, es decir, se usa la misma clave para firmar y verificar el token.

    public String getUsernameFromToken(String token){
        return getClaimFromToken(token, Claims::getSubject);
    }

    //Si en algun momento futuro al copiar este codigo da error, revisa que sea
    //import java.util.Date puede que estes importando java.sql.Date
    //Eso da error
    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    //Traer los datos que se encuentrar en el JWT
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver){
        //Claims es una interfaz que extiende Map<String, Object>
        //y contiene los datos que se encuentran dentro del JWT. 
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token){
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(expiration);
    }

    public String generateToken(UserDetails userDetails){
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder().setClaims(claims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000)).signWith(key).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails){
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    //Refrescando token por las mismas 3 horas
    public String generateRefreshToken(UserDetails userDetails) {
        return Jwts.builder()
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
            .signWith(key)
            .compact();
    }
}
