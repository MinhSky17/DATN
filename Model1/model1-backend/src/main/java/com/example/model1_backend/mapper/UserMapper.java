//package com.example.model1_backend.mapper;
//
//import com.example.model1_backend.dto.request.UserCreationRequest;
//import com.example.model1_backend.dto.request.UserUpdateRequest;
//import com.example.model1_backend.dto.response.UserResponse;
//import com.example.model1_backend.entity.User;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//import org.mapstruct.MappingTarget;
//
//import java.util.List;
//
//@Mapper(componentModel = "spring")
//public interface UserMapper {
//    User toUser(UserCreationRequest request);
//
//    //@Mapping(source = "firstName", target = "lastName")
//    //@Mapping(target = "lastName", ignore = true) //lastName = null
//    UserResponse toUserResponse(User user);
//
//    List<UserResponse> toUsersResponse(List<User> users);
//
//
//    void updateUser(@MappingTarget User user, UserUpdateRequest request);
//}
