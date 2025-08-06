package com.backend.platformDuoc.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.backend.platformDuoc.models.User;
import com.backend.platformDuoc.repository.UserRepository;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    final UserRepository userRepository;

    public JwtUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);

        if(user == null){
            throw new UsernameNotFoundException("Usuario no encontrado con username: " + username);
        }

        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("USER_ROLE"));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorityList);
    }

    // public UserDetails loadUserByEmail(String email) {
    //     User user = userRepository.findByEmail(email);

    //     if(user == null){
    //         throw new UsernameNotFoundException("Usuario no encontrado con username: " + email);
    //     }

    //     List<GrantedAuthority> authorityList = new ArrayList<>();
    //     authorityList.add(new SimpleGrantedAuthority("USER_ROLE"));

    //     return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorityList);
    // }
}
