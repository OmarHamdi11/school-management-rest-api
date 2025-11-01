package com.ellafy.school_management_jpa_hibernate.service;

import com.ellafy.school_management_jpa_hibernate.payload.JwtAuthResponse;
import com.ellafy.school_management_jpa_hibernate.payload.LoginDto;
import com.ellafy.school_management_jpa_hibernate.payload.RegisterDto;

public interface AuthService {

    JwtAuthResponse login(LoginDto loginDto);

    String register(RegisterDto registerDto);

}
