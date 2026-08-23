package com.bacheloros.bacheloros_backend.controller;

import com.bacheloros.bacheloros_backend.dto.CreateUserRequest;
import com.bacheloros.bacheloros_backend.dto.UserResponse;
import com.bacheloros.bacheloros_backend.entity.User;
import com.bacheloros.bacheloros_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService)
    {
        this.userService=userService;
    }
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@RequestBody CreateUserRequest request)
    {
        User created = userService.createUser(request);
        UserResponse response = new UserResponse(created.getId(), created.getEmail(), created.getName(),created.getPhone());
        return ResponseEntity.ok(response);
    }

}
