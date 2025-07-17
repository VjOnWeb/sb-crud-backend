 package com.vijay.crudApi.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vijay.crudApi.Repo.AppUserRepository;
import com.vijay.crudApi.models.AppUsers;
import com.vijay.crudApi.models.AuthRequest;
import com.vijay.crudApi.models.AuthResponse;
import com.vijay.crudApi.models.RegisterRequest;
import com.vijay.crudApi.utils.JwtUtil;
@RestController
@RequestMapping("/api/auth")
@CrossOrigin("http://localhost:3838")

public class AuthController {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtUtil jwtUtil;
    @Autowired private AppUserRepository userRepo;
    @Autowired private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    authRequest.getEmail(),
                    authRequest.getPassword()
                )
            );

            AppUsers user = userRepo.findByEmail(authRequest.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            String token = jwtUtil.generateToken(user);

            return ResponseEntity.ok(new AuthResponse(token));

        } catch (Exception ex) {
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid credentials");
        }
    }


    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        AppUsers user = userRepo.findByEmail(email).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();

        user.setPassword(null); // don’t return password
        return ResponseEntity.ok(user);
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateProfile(Authentication authentication, @RequestBody AppUsers updatedUser) {
        String email = authentication.getName();
        AppUsers user = userRepo.findByEmail(email).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();

        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        userRepo.save(user);
        return ResponseEntity.ok("Profile updated");
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (userRepo.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest().body("Email already in use");
        }

        AppUsers user = new AppUsers();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setUsername(request.getUserName());
        user.setRole(request.getRole());

        userRepo.save(user);
        return ResponseEntity.ok("User registered successfully");
    }
    @PutMapping("/users/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody AppUsers updatedUser, Principal principal) {
        String email = principal.getName();
        AppUsers existingUser = userRepo.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        userRepo.save(existingUser);
        return ResponseEntity.ok(existingUser);
    }
}
