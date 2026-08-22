package com.bacheloros.bacheloros_backend.controller;

import com.bacheloros.bacheloros_backend.dto.CreateUserRequest;
import com.bacheloros.bacheloros_backend.dto.LoginRequest;
import com.bacheloros.bacheloros_backend.dto.AuthResponse;
import com.bacheloros.bacheloros_backend.entity.User;
import com.bacheloros.bacheloros_backend.repository.UserRepository;
import com.bacheloros.bacheloros_backend.service.JwtService;
import com.bacheloros.bacheloros_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, JwtService jwtService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //SignUp
    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUp( @RequestBody CreateUserRequest request)
    {
        User user=userService.createUser(request);
        String token=jwtService.generateToken(user);
        return ResponseEntity.ok(new AuthResponse(token));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request)
    {
        User user=userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));
        String token=jwtService.generateToken(user);
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid email or password");
        }
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
