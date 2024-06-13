package com.whatsapp.Whatsappclone.Controllers;

import com.whatsapp.Whatsappclone.Dto.AuthenticationResponse;
import com.whatsapp.Whatsappclone.Dto.LoginRequest;
import com.whatsapp.Whatsappclone.Dto.RegistrationRequest;
import com.whatsapp.Whatsappclone.Models.AppUser;
import com.whatsapp.Whatsappclone.Repositories.UserRepository;
import com.whatsapp.Whatsappclone.Security.JWT.JwtTokenUtil;
import com.whatsapp.Whatsappclone.Security.UserRole;
import com.whatsapp.Whatsappclone.Services.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    /**
     * Endpoint for user registration.
     * Registers a new user with the provided registration details.
     *
     * @param request the registration request containing user details
     * @return authentication response with JWT token
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegistrationRequest request) {
        log.info("Registration request received for user: {}", request.getEmail());

        // Create a new AppUser object and populate it with registration details
        AppUser user = new AppUser();
        user.setUsername(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Encrypt password
        user.setRole(UserRole.ROLE_USER); // Set default role
        user.setFullName(request.getFullName());

        // Save the user to the database
        userRepository.save(user);

        // Authenticate the user after registration
        Authentication authentication = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Load UserDetails for the authenticated user
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        // Generate JWT token for the user
        String token = "Bearer " + jwtTokenUtil.generateToken(userDetails);

        // Create authentication response with token
        AuthenticationResponse response = new AuthenticationResponse(token, true);

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint for user login.
     * Authenticates a user with the provided login credentials.
     *
     * @param request the login request containing user credentials
     * @return authentication response with JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) {
        log.info("Login request received for user: {}", request.getEmail());

        // Authenticate user with provided credentials
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        // Load UserDetails for the authenticated user
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

        // Generate JWT token for the user
        String token = "Bearer " + jwtTokenUtil.generateToken(userDetails);

        // Create authentication response with token
        AuthenticationResponse response = new AuthenticationResponse(token, true);

        return ResponseEntity.ok(response);
    }
}
