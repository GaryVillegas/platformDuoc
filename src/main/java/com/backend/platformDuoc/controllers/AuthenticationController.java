package com.backend.platformDuoc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.platformDuoc.models.Rol;
import com.backend.platformDuoc.models.User;
import com.backend.platformDuoc.repository.UserRepository;
import com.backend.platformDuoc.services.JwtUserDetailsService;
import com.backend.platformDuoc.services.RoleService;
import com.backend.platformDuoc.utils.JwtTokenUtil;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    protected final Log logger = LogFactory.getLog(getClass());

    final UserRepository userRepository;
    final AuthenticationManager authenticationManager;
    final JwtUserDetailsService userDetailsService;
    final JwtTokenUtil jwtTokenUtil;

    public AuthenticationController(UserRepository userRepository, AuthenticationManager authenticationManager, JwtUserDetailsService userDetailsService, JwtTokenUtil jwtTokenUtil){
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody Map<String, String> loginData){
        String username = loginData.get("username");
        String password = loginData.get("password");
        Map<String, Object> responseMap = new HashMap<>();

        try{
            Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            if(auth.isAuthenticated()){
                logger.info("Logged In");
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                String token = jwtTokenUtil.generateToken(userDetails);
                responseMap.put("success", true);
                responseMap.put("message", "Logged in successfully.");
                responseMap.put("token", token);
                return ResponseEntity.ok(responseMap);
            } else {
                responseMap.put("success", false);
                responseMap.put("message", "Invalid Credentials");
                return ResponseEntity.status(401).body(responseMap);
            }
        } catch (DisabledException e){
            e.printStackTrace();
            responseMap.put("success", false);
            responseMap.put("message", "User is disabled.");
            return ResponseEntity.status(500).body(responseMap);
        } catch(BadCredentialsException e){
            responseMap.put("success", false);
            responseMap.put("message", "Invalid Credentials.");
            return ResponseEntity.status(401).body(responseMap);
        } catch (Exception e){
            responseMap.put("success", false);
            responseMap.put("message", "Something went wrong.");
            return ResponseEntity.status(500).body(responseMap);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody Map<String, String> userData) {
        String username = userData.get("username");
        String email = userData.get("email");
        String password = userData.get("password");
        String name = userData.get("name");
        String lastname = userData.get("name");
        String role_id = userData.get("role_id");

        Map<String, Object> responseMap = new HashMap<>();
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setName(name);
        user.setLastname(lastname);
        if(role_id == null){
            responseMap.put("error", true);
            responseMap.put("message", "El campo 'roleId' es obligatorio.");
            return ResponseEntity.badRequest().body(responseMap);
        }

        Integer roleId;
        try {
        roleId = Integer.parseInt(role_id);
        } catch (NumberFormatException e) {
            responseMap.put("error", true);
            responseMap.put("message", "El 'roleId' debe ser un número válido.");
            return ResponseEntity.badRequest().body(responseMap);
        }

        Rol rol = roleService.findById(roleId);
        if (rol == null) {
            responseMap.put("error", true);
            responseMap.put("message", "Rol con ID " + roleId + " no encontrado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseMap);
        }
        user.setRole(rol);
        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String token = jwtTokenUtil.generateToken(userDetails);
        
        responseMap.put("error", false);
        responseMap.put("message", "Account created successfully");
        responseMap.put("token", token);
        return ResponseEntity.ok(responseMap);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request){
        String authorizationHeader = request.getHeader("Authorization");

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer")){
            String refreshToken = authorizationHeader.substring(7);
            String username = jwtTokenUtil.getUsernameFromToken(refreshToken);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if(jwtTokenUtil.validateToken(refreshToken, userDetails)){
                String newAccessToken = jwtTokenUtil.generateRefreshToken(userDetails);
                return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
            }
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token inválido o expirado");
    }

}
