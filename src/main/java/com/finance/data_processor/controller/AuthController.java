package com.finance.data_processor.controller;

import com.finance.data_processor.dto.JwtResponse;
import com.finance.data_processor.dto.LoginRequest;
import com.finance.data_processor.model.Role;
import com.finance.data_processor.model.RoleName;
import com.finance.data_processor.model.User;
import com.finance.data_processor.model.UserStatus;
import com.finance.data_processor.repository.RoleRepository;
import com.finance.data_processor.repository.UserRepository;
import com.finance.data_processor.security.CustomUserDetailsService;
import com.finance.data_processor.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    // 1. REGISTER A NEW USER
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody LoginRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Username is already taken!");
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        // ENCRYPT THE PASSWORD BEFORE SAVING!
        newUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        newUser.setStatus(UserStatus.ACTIVE);

        Role viewerRole = roleRepository.findByName(RoleName.VIEWER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        newUser.setRoles(Set.of(viewerRole));
        userRepository.save(newUser);

        return ResponseEntity.ok("User registered successfully!");
    }

    // 2. LOGIN AND GET TOKEN
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody LoginRequest request) throws Exception {
        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect username or password");
        }


        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(jwt));
    }
}
