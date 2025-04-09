package com.example.model1_backend.controller;

import com.example.model1_backend.dto.request.ApiResponse;
import com.example.model1_backend.dto.request.UserCreationRequest;
import com.example.model1_backend.dto.request.UserUpdateRequest;
import com.example.model1_backend.dto.response.UserResponse;
import com.example.model1_backend.entity.User;
import com.example.model1_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping()
    ApiResponse<User> createUser(@RequestBody @Valid UserCreationRequest request){
        ApiResponse<User> apiResponse = new ApiResponse<>();

        apiResponse.setResult(userService.createUser(request));
        return apiResponse;
    }

    @GetMapping()
    List<User> getUser(){
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    UserResponse getUser(@PathVariable("userId") String userId){
        return userService.getUser(userId);
    }

    @PutMapping("/{userId}")
    UserResponse updateUser(@PathVariable("userId") String userId, @RequestBody UserUpdateRequest request){
        return userService.updateUser(userId, request);
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable("userId") String userId){
        userService.deteleUser(userId);
        return "User has been deleted";
    }
}
