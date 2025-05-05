package com.model1.application;

import com.model1.application.entity.User;
import com.model1.application.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Timestamp;
import java.util.Collections;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public CommandLineRunner initAdmin(UserRepository userRepository,
                                       PasswordEncoder passwordEncoder) {
        return args -> {
            String adminEmail = "admin@gmail.com";
            if (userRepository.findByEmail(adminEmail) != null) {
                System.out.println("Admin user already exists");
                return;
            }

            // Tạo và mã hóa mật khẩu
            String encodedPassword = passwordEncoder.encode("admin@123");

            User admin = new User();
            admin.setFullName("Admin");
            admin.setEmail(adminEmail);
            admin.setPassword(encodedPassword);
            admin.setPhone("");
            admin.setAddress("");
            admin.setAvatar(null);
            admin.setRoles(Collections.singletonList("ADMIN"));
            admin.setStatus(true);
            Timestamp now = new Timestamp(System.currentTimeMillis());
            admin.setCreatedAt(now);
            admin.setModifiedAt(now);

            userRepository.save(admin);
            System.out.println("Admin user created: " + adminEmail);
        };
    }
}
