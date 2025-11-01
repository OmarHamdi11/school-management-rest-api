package com.springboot.school_management.service;

import com.springboot.school_management.payload.JwtAuthResponse;
import com.springboot.school_management.payload.LoginDto;
import com.springboot.school_management.payload.RegisterDto;

public interface AuthService {

    JwtAuthResponse login(LoginDto loginDto);

    String register(RegisterDto registerDto);

}
