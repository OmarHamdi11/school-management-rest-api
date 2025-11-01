package com.springboot.school_management.payload;

import com.springboot.school_management.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {

    private String username;

    private String password;

    private Role role;

    private String specialization;

    private String major;

}
