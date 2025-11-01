package com.springboot.school_management.service.impl;

import com.springboot.school_management.entity.Instructor;
import com.springboot.school_management.entity.Role;
import com.springboot.school_management.entity.Student;
import com.springboot.school_management.entity.User;
import com.springboot.school_management.payload.JwtAuthResponse;
import com.springboot.school_management.payload.LoginDto;
import com.springboot.school_management.payload.RegisterDto;
import com.springboot.school_management.repository.UserRepository;
import com.springboot.school_management.security.JwtTokenProvider;
import com.springboot.school_management.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    public JwtAuthResponse login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authentication);


        String token = jwtTokenProvider.generateToken(authentication);
        User user = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        JwtAuthResponse response = new JwtAuthResponse();
        response.setAccessToken(token);
        response.setUsername(user.getUsername());
        response.setRole(user.getRole());

        return response;
    }

    @Override
    public String register(RegisterDto registerDto) {

        // add check for username exist in db
        if (userRepository.existsByUsername(registerDto.getUsername())){
            throw new RuntimeException("Username is already exists");
        }

        // Create user based on role
        User user;
        if (registerDto.getRole() == Role.INSTRUCTOR) {
            Instructor instructor = new Instructor();
            instructor.setSpecialization(registerDto.getSpecialization());
            user = instructor;
        } else {
            Student student = new Student();
            student.setMajor(registerDto.getMajor());
            user = student;
        }

        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRole(registerDto.getRole());

        userRepository.save(user);

        return "User registered successfully";
    }
}
