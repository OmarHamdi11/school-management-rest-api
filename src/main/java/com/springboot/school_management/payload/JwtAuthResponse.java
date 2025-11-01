package com.springboot.school_management.payload;

import com.springboot.school_management.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JwtAuthResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private String username;
    private Role role;
}
