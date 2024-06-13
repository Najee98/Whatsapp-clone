package com.whatsapp.Whatsappclone.Services;

import com.whatsapp.Whatsappclone.Dto.UpdateUserRequest;
import com.whatsapp.Whatsappclone.Exceptions.CustomExceptions.UserException;
import com.whatsapp.Whatsappclone.Models.AppUser;
import com.whatsapp.Whatsappclone.Repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return the user if found
     * @throws UserException if no user is found with the given ID
     */
    @Override
    public AppUser findUserById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserException("User with id: " + id + " not found"));
    }

    /**
     * Searches for users based on a search query.
     * If searchQuery is null, retrieves all users.
     *
     * @param searchQuery the search query
     * @return list of users matching the search criteria
     */
    @Override
    public List<AppUser> searchUser(String searchQuery) {
        List<AppUser> users;
        AppUser requestUser = findUserProfile();

        if (searchQuery == null) {
            users = userRepository.findAll();
        } else {
            users = userRepository.searchUser(searchQuery, requestUser.getId());
        }

        return users;
    }

    /**
     * Retrieves the current authenticated user profile.
     *
     * @return the authenticated user profile
     * @throws BadCredentialsException if the user is not authenticated
     *                                 or if no user is found with the authenticated username
     */
    @Override
    public AppUser findUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (username == null) {
            throw new BadCredentialsException("Invalid token received.");
        }

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserException("User with username: " + username + " not found."));
    }

    /**
     * Updates a user's information based on the given ID and update request.
     *
     * @param id      the ID of the user to update
     * @param request the update request containing new user information
     * @return the updated user object
     * @throws UserException if no user is found with the given ID
     */
    @Override
    public AppUser updateUser(Integer id, UpdateUserRequest request) throws UserException {
        AppUser user = findUserById(id);

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }

        if (request.getProfilePicture() != null) {
            user.setProfilePicture(request.getProfilePicture());
        }

        return userRepository.save(user);
    }

    /**
     * Finds potential target users for a chat based on chat ID and user.
     *
     * @param id   the ID of the chat
     * @param user the user initiating the search
     * @return list of potential target users for the chat
     */
    @Override
    public List<AppUser> findChatTargetUser(Integer id, AppUser user) {
        return userRepository.findChatTargetUser(id, user);
    }
}
