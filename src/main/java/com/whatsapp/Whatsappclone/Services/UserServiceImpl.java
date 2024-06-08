package com.whatsapp.Whatsappclone.Services;

import com.whatsapp.Whatsappclone.Dto.UpdateUserRequest;
import com.whatsapp.Whatsappclone.Exceptions.CustomExceptions.UserException;
import com.whatsapp.Whatsappclone.Models.AppUser;
import com.whatsapp.Whatsappclone.Repositories.UserRepository;
import com.whatsapp.Whatsappclone.Security.JWT.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @Override
    public AppUser findUserById(Integer id) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new UserException("User with id: " + id + " not found"));

        return user;
    }

    @Override
    public List<AppUser> searchUser(String searchQuery) {

        List<AppUser> users = new ArrayList<>();

        if (searchQuery == null)
            users = userRepository.findAll();
        else
            users = userRepository.searchUser(searchQuery);

        return users;
    }

    @Override
    public AppUser findUserProfile(String jwt) {
        String email = tokenProvider.getEmailFromToken(jwt);

        if(email == null)
            throw new BadCredentialsException("Invalid token received.");

        AppUser user = userRepository.findByEmail(email);

        if (user == null)
            throw new UserException("User with email: " + email + " not found.");

        return user;
    }

    @Override
    public AppUser updateUser(Integer id, UpdateUserRequest request) throws UserException {
        AppUser user = findUserById(id);

        if (request.getFullName() != null)
            user.setFullName(request.getFullName());

        if (request.getProfilePicture() != null)
            user.setProfilePicture(request.getProfilePicture());

        return userRepository.save(user);
    }

    @Override
    public AppUser findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }
}
