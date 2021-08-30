package com.minihcl.security;

import com.minihcl.security.Service.UserService;
import com.minihcl.security.model.AppUsers;
import com.minihcl.security.model.Roles;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class SecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner runner(UserService userService){
        return args -> {
            userService.saveRoles(new Roles(null,"ROLE_USER"));
            userService.saveRoles(new Roles(null,"ROLE_MANAGER"));
            userService.saveRoles(new Roles(null,"ROLE_ADMIN"));
            userService.saveRoles(new Roles(null,"ROLE_SUPER_ADMIN"));

            userService.saveusers(new AppUsers(null,"Raj","Raj","12345",new ArrayList<>()));
            userService.saveusers(new AppUsers(null,"sham","shamRaj","987654",new ArrayList<>()));
            userService.saveusers(new AppUsers(null,"manu","manuRaj","manu123",new ArrayList<>()));
            userService.saveusers(new AppUsers(null,"shesha","rocky","raj123",new ArrayList<>()));

            userService.addRoleToUser("Raj","ROLE_USER");
            userService.addRoleToUser("manu","ROLE_ADMIN");
            userService.addRoleToUser("manu","ROLE_MANAGER");
            userService.addRoleToUser("shesha","ROLE_SUPER_ADMIN");
            userService.addRoleToUser("sham","ROLE_MANAGER");

        };
    }
}
