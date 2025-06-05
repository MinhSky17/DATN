package com.model1.application.service.impl;

import com.model1.application.entity.Brand;
import com.model1.application.entity.User;
import com.model1.application.exception.BadRequestException;
import com.model1.application.exception.InternalServerException;
import com.model1.application.exception.NotFoundException;
import com.model1.application.model.dto.UserDTO;
import com.model1.application.model.mapper.UserMapper;
import com.model1.application.model.request.*;
import com.model1.application.repository.UserRepository;
import com.model1.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.model1.application.config.Contant.LIMIT_USER;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<UserDTO> getListUsers() {
        List<User> users = userRepository.findAll();
        List<UserDTO> userDTOS = new ArrayList<>();
        for (User user : users) {
            userDTOS.add(UserMapper.toUserDTO(user));
        }
        return userDTOS;
    }

    @Override
    public User findByUserId(Long id){
        return userRepository.findById(id).orElseThrow(()-> new RuntimeException("Khong tim thay user"));
    }

    @Override
    public Page<User> adminListUserPages(String fullName, String phone, String email, Integer page) {
        page--;
        if (page < 0) {
            page = 0;
        }
        Pageable pageable = PageRequest.of(page, LIMIT_USER, Sort.by("created_at").descending());
        return userRepository.adminListUserPages(fullName, phone, email, pageable);
    }

    @Override
    public User createUser(CreateUserRequest createUserRequest) {
        User user = userRepository.findByEmail(createUserRequest.getEmail());
        if (user != null) {
            throw new BadRequestException("Email đã tồn tại trong hệ thống. Vui lòng sử dụng email khác!");
        }
        user = UserMapper.toUser(createUserRequest);
        userRepository.save(user);
        return user;
    }



    @Override
    public void changePassword(User user, ChangePasswordRequest changePasswordRequest) {
        //Kiểm tra mật khẩu
        if (!BCrypt.checkpw(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Mật khẩu cũ không chính xác");
        }

        String hash = BCrypt.hashpw(changePasswordRequest.getNewPassword(), BCrypt.gensalt(12));
        user.setPassword(hash);
        userRepository.save(user);
    }

    @Override
    public User updateProfile(User user, UpdateProfileRequest updateProfileRequest) {
        user.setFullName(updateProfileRequest.getFullName());
        user.setPhone(updateProfileRequest.getPhone());
        user.setAddress(updateProfileRequest.getAddress());

        return userRepository.save(user);
    }

    @Override
    public User getUserById(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("Tài khoản không tồn tại!");
        }
        return user.get();
    }

    @Override
    public User taoUser(CrUserRequest crUserRequest) {
        User user = userRepository.findByEmail(crUserRequest.getEmail());
        if (user != null) {
            throw new BadRequestException("Email đã tồn tại trong hệ thống. Vui lòng sử dụng email khác!");
        }
        user = UserMapper.toUser2(crUserRequest);
        userRepository.save(user);
        return user;
    }

    @Override
    public void updateUser(CrUserRequest crUserRequest, Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("Tài khoản không tồn tại!");
        }
        User existingUser = userOptional.get();

        // Kiểm tra email trùng, loại trừ email của user hiện tại
        User userByEmail = userRepository.findByEmail(crUserRequest.getEmail());
        if (userByEmail != null && !(userByEmail.getId() == id)) {
            throw new BadRequestException("Email đã tồn tại trong hệ thống. Vui lòng sử dụng email khác!");
        }

        existingUser.setFullName(crUserRequest.getFullName());
        existingUser.setEmail(crUserRequest.getEmail());
        existingUser.setPhone(crUserRequest.getPhone());
        existingUser.setAvatar(crUserRequest.getAvatar());
        existingUser.setModifiedAt(new Timestamp(System.currentTimeMillis()));

        try {
            userRepository.save(existingUser);
        } catch (Exception ex) {
            throw new InternalServerException("Lỗi khi sửa tài khoản");
        }
    }

    @Override
    public void deleteUser(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundException("Tài khoản không tồn tại!");
        }
        try {
            userRepository.deleteById(id);
        } catch (Exception ex) {
            throw new InternalServerException("Lỗi khi xóa tài khoản!");
        }
    }
}
