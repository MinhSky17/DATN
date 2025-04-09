package com.example.model1_backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class UserCreationRequest {
    int roleId;

    @Size(min = 3, message = "USERNAME_INVALID")
    String username;

    @Size(min = 8, message = "PASSWORD_INVALID")
    String password;
    String firstName;
    String lastName;
    LocalDate dateOfBirth;

    @Email(message = "Invalid email format")
    String email;
    String phoneNumber;
    String gender;
    String address;
    Boolean active = true;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}
