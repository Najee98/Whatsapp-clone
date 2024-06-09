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

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegistrationRequest request) {
        log.info("Registration request received for user: {}", request.getEmail());

        AppUser user = new AppUser();

        user.setUsername(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.ROLE_USER);
        user.setFullName(request.getFullName());

        userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = "Bearer " + jwtTokenUtil.generateToken(userDetails);

        AuthenticationResponse response = new AuthenticationResponse(token, true);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) {
        log.info("Login request received for user: {}", request.getEmail());
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = "Bearer " + jwtTokenUtil.generateToken(userDetails);

        AuthenticationResponse response = new AuthenticationResponse(token, true);

        return ResponseEntity.ok(response);

    }
}
