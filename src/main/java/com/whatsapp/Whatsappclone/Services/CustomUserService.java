//package com.whatsapp.Whatsappclone.Services;
//
//import com.whatsapp.Whatsappclone.Models.AppUser;
//import com.whatsapp.Whatsappclone.Repositories.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class CustomUserService implements UserDetailsService {
//
//    private final UserRepository userRepository;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        AppUser user = userRepository.findByEmail(username);
//
//        if (user == null)
//            throw new UsernameNotFoundException("User with username " + username + " not found");
//
//        List<GrantedAuthority> authorities = new ArrayList<>();
//
//        return new User(user.getEmail(), user.getPassword(), authorities);
//    }
//}
