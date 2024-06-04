package com.whatsapp.Whatsappclone.Controllers;

import com.whatsapp.Whatsappclone.Dto.AuthenticationResponse;
import com.whatsapp.Whatsappclone.Dto.LoginRequest;
import com.whatsapp.Whatsappclone.Dto.RegistrationRequest;
import com.whatsapp.Whatsappclone.Exceptions.CustomExceptions.UserException;
import com.whatsapp.Whatsappclone.Models.AppUser;
import com.whatsapp.Whatsappclone.Repositories.UserRepository;
import com.whatsapp.Whatsappclone.Security.JWT.TokenProvider;
import com.whatsapp.Whatsappclone.Services.CustomUserService;
import jakarta.servlet.Registration;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final CustomUserService customUserService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegistrationRequest userRequest) {
        AppUser isUser = userRepository.findByEmail(userRequest.getEmail());

        if (isUser != null)
            throw new UserException("Email already taken.");

        AppUser user = new AppUser();

        user.setEmail(userRequest.getEmail());
        user.setFullName(userRequest.getFullName());
        //     user.setProfilePicture(user.getProfilePicture());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        userRepository.save(user);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userRequest.getEmail(), userRequest.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        AuthenticationResponse response = new AuthenticationResponse(jwt, true);

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request){

        Authentication authentication = authenticate(request.getEmail(), request.getPassword());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        AuthenticationResponse response = new AuthenticationResponse(jwt, true);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public Authentication authenticate (String username, String password){
        UserDetails userDetails = customUserService.loadUserByUsername(username);

        if (userDetails == null)
            throw new BadCredentialsException("Invalid username.");

        if (!passwordEncoder.matches(password, userDetails.getPassword()))
            throw new BadCredentialsException("Invalid password.");

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
