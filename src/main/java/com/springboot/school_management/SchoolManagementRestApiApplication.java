package com.springboot.school_management;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SchoolManagementRestApiApplication {

    @Bean
    ModelMapper modelMapper(){
        return new ModelMapper();
    }

	public static void main(String[] args) {
		SpringApplication.run(SchoolManagementRestApiApplication.class, args);
	}

}
