package com.example.model1_backend.service;

import com.example.model1_backend.dto.request.UserCreationRequest;
import com.example.model1_backend.dto.request.UserUpdateRequest;
import com.example.model1_backend.dto.response.UserResponse;
import com.example.model1_backend.entity.User;
import com.example.model1_backend.exception.AppException;
import com.example.model1_backend.exception.ErrorCode;
import com.example.model1_backend.mapper.UserMapper;
import com.example.model1_backend.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

public class UserService {
    /*@Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;*/

    UserRepository userRepository;
    UserMapper userMapper;

    /*public User createUser(UserCreationRequest request){
        User user = new User();

        if(userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        user.setRoleId(request.getRoleId());
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setDateOfBirth(request.getDateOfBirth());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setGender(request.getGender());
        user.setAddress(request.getAddress());
        user.setActive(request.getActive());
        user.setCreatedAt(request.getCreatedAt());
        user.setUpdatedAt(request.getUpdatedAt());

        return userRepository.save(user);
    }*/

    //Mapstuct lo het
    public User createUser(UserCreationRequest request){

        if(userRepository.existsByUsername(request.getUsername()))
            throw new AppException(ErrorCode.USER_EXISTED);

        User user = userMapper.toUser(request);

        return userRepository.save(user);
    }

    public List<User> getUsers(){
        return userRepository.findAll();
    }

    public UserResponse getUser(String id){
        return userMapper.toUserResponse(userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found")));
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        userMapper.updateUser(user, request);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deteleUser(String userId){
        userRepository.deleteById(userId);
    }
}
