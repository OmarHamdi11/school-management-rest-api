package com.springboot.school_management.security;

import com.springboot.school_management.entity.User;
import com.springboot.school_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //System.out.println("Loading user: " + username);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User Not Found"));
        //System.out.println("User found: " + user.getUsername() + ", Role: " + user.getRole());


        //GrantedAuthority authorities = new SimpleGrantedAuthority(user.getRole().name());
        Set<GrantedAuthority> authorities = Set.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}
