package com.bacheloros.bacheloros_backend.controller;

import com.bacheloros.bacheloros_backend.dto.CreateUserRequest;
import com.bacheloros.bacheloros_backend.dto.UserResponse;
import com.bacheloros.bacheloros_backend.entity.User;
import com.bacheloros.bacheloros_backend.repository.UserRepository;
import com.bacheloros.bacheloros_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    public UserController(UserService userService, UserRepository userRepository)
    {
        this.userService=userService;
        this.userRepository = userRepository;
    }
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request)
    {
        User created = userService.createUser(request);
        UserResponse response = new UserResponse(created.getId(), created.getEmail(), created.getName(),created.getPhone());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser()
    {
        String email= SecurityContextHolder.getContext().getAuthentication().getName();
        User user=userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        UserResponse response = new UserResponse(user.getId(), user.getEmail(), user.getName(),user.getPhone());
        return ResponseEntity.ok(response);
    }

}
