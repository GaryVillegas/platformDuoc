package com.backend.platformDuoc.utils;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.backend.platformDuoc.services.JwtUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter{

    private final JwtUserDetailsService jwtUserDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    public JwtRequestFilter(JwtUserDetailsService jwtUserDetailsService, JwtTokenUtil jwtTokenUtil){
        this.jwtUserDetailsService = jwtUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException{
        // Obtiene el valor del encabezado Authorization del request HTTP
        final String requestTokenHeader = request.getHeader("Authorization");
        // Verifica si el token no es nulo y comienza con "Bearer "
        if(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")){
            // Extrae el token JWT eliminando la parte "Bearer "
            String jwtToken = requestTokenHeader.substring(7);
            try{
                // Extrae el email desde el JWT (guardado como subject)
                String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
                // Verifica que el email no sea nulo y que no haya ya una autenticación en el contexto de seguridad
                if(username != null && !username.trim().isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Carga los datos del usuario usando el email extraído del token
                    UserDetails userDetails = jwtUserDetailsService.loadUserByUsername(username);
                    // Valida que el token sea válido para ese usuario
                    if(jwtTokenUtil.validateToken(jwtToken, userDetails)){
                        // Crea el token de autenticación con las credenciales del usuario y sus roles
                        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        // Asocia detalles adicionales del request (como IP, sesión, etc.)
                        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        // Establece la autenticación en el contexto de seguridad de Spring
                        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                    }
                }
            } catch (IllegalArgumentException e) {
                // Captura errores al intentar obtener datos del token
                logger.error("Unable to fetch JWT Token");
            } catch (ExpiredJwtException e) {
                // Captura errores si el token ha expirado
                logger.error("JWT Token is expired");
            } catch (Exception e) {
                // Captura cualquier otro error inesperado
                logger.error(e.getMessage());
            }
        } else {
            // Si el encabezado no empieza con "Bearer ", se escribe una advertencia en los logs
            logger.warn("JWT Token does not begin with Bearer String");
        }
        // Continúa con el resto del filtro de la cadena (permite que otras partes del request sigan su curso)
        chain.doFilter(request, response);
    }
}
