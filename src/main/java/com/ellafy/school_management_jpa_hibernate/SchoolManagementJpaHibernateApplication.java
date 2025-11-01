package com.ellafy.school_management_jpa_hibernate;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SchoolManagementJpaHibernateApplication {

    @Bean
    ModelMapper modelMapper(){
        return new ModelMapper();
    }

	public static void main(String[] args) {
		SpringApplication.run(SchoolManagementJpaHibernateApplication.class, args);
	}

}
