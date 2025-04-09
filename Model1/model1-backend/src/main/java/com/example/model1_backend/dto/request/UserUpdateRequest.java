package com.example.model1_backend.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class UserUpdateRequest {
    int roleId;
    String username;
    String password;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;
    String email;
    String phoneNumber;
    String gender;
    String address;
    Boolean active = true;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}
