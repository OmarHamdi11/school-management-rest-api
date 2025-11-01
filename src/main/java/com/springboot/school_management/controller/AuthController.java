package com.springboot.school_management.controller;


import com.springboot.school_management.payload.JwtAuthResponse;
import com.springboot.school_management.payload.LoginDto;
import com.springboot.school_management.payload.RegisterDto;
import com.springboot.school_management.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(value = {"/login", "/signin"})
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginDto loginDto){
        return new ResponseEntity<>(authService.login(loginDto), HttpStatus.OK);
    }


    @PostMapping(value = {"/register", "/signup"})
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
        return new ResponseEntity<>(authService.register(registerDto), HttpStatus.CREATED);
    }

}
