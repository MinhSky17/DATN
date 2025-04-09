package com.example.model1_backend.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class UserResponse {
    private String id;
    private int roleId = 0;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String email;
    private String phoneNumber;
    private String gender;
    private String address;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
