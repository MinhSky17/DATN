package com.model1.application.controller.admin;

import com.model1.application.entity.Brand;
import com.model1.application.entity.User;
import com.model1.application.model.mapper.UserMapper;
import com.model1.application.model.request.CrUserRequest;
import com.model1.application.model.request.CreateBrandRequest;
import com.model1.application.model.request.CreateUserRequest;
import com.model1.application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class AdminUserController {

    @Autowired
    private UserService userService;

    @GetMapping("/admin/users")
    public String homePages(Model model,
                            @RequestParam(defaultValue = "", required = false) String fullName,
                            @RequestParam(defaultValue = "", required = false) String phone,
                            @RequestParam(defaultValue = "", required = false) String email,
                            @RequestParam(defaultValue = "", required = false) String address,
                            @RequestParam(defaultValue = "1", required = false) Integer page) {
        Page<User> users = userService.adminListUserPages(fullName, phone, email, page);
        model.addAttribute("users", users.getContent());
        model.addAttribute("totalPages", users.getTotalPages());
        model.addAttribute("currentPage", users.getPageable().getPageNumber() + 1);
        return "admin/user/list";
    }

    @GetMapping("/api/admin/users/list")
    public ResponseEntity<Object> getListUserPages(@RequestParam(defaultValue = "", required = false) String fullName,
                                                   @RequestParam(defaultValue = "", required = false) String phone,
                                                   @RequestParam(defaultValue = "", required = false) String email,
                                                   @RequestParam(defaultValue = "", required = false) String address,
                                                   @RequestParam(defaultValue = "1", required = false) Integer page) {
        Page<User> users = userService.adminListUserPages(fullName, phone, email, page);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/api/admin/user")
    public ResponseEntity<Object> createUser(@Valid @RequestBody CrUserRequest crUserRequest){
        User user = userService.taoUser(crUserRequest);
        return ResponseEntity.ok(UserMapper.toUserDTO(user));
    }

    @GetMapping("/api/admin/users/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable long id){
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/api/admin/users/{id}")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody CrUserRequest crUserRequest, @PathVariable long id) {
        userService.updateUser(crUserRequest, id);
        return ResponseEntity.ok("Sửa tài khoản thành công!");
    }

    @DeleteMapping("/api/admin/users/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("Xóa tài khoản thành công!");
    }
}
