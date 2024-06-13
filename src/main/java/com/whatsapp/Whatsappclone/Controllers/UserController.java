package com.whatsapp.Whatsappclone.Controllers;

import com.whatsapp.Whatsappclone.Dto.UpdateUserRequest;
import com.whatsapp.Whatsappclone.Dto.UpdateUserResponse;
import com.whatsapp.Whatsappclone.Models.AppUser;
import com.whatsapp.Whatsappclone.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Endpoint to search for users based on a search query.
     *
     * @param searchQuery the query string to search users
     * @return ResponseEntity with a list of AppUser objects
     */
    @GetMapping()
    public ResponseEntity<List<AppUser>> searchUsers(@RequestParam String searchQuery){
        List<AppUser> users = userService.searchUser(searchQuery);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Endpoint to fetch the user profile of the authenticated user.
     *
     * @param token JWT token in the authorization header
     * @return ResponseEntity with the AppUser object representing the user profile
     */
    @GetMapping("/profile")
    public ResponseEntity<AppUser> getUserProfile(@RequestHeader("Authorization") String token){

        AppUser user = userService.findUserProfile();

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Endpoint to update the user profile of the authenticated user.
     *
     * @param request UpdateUserRequest object containing updated user details
     * @param token   JWT token in the authorization header
     * @return ResponseEntity with UpdateUserResponse indicating success or failure of the update
     */
    @PutMapping("/update")
    public ResponseEntity<UpdateUserResponse> updateUserProfile(
            @RequestBody UpdateUserRequest request,
            @RequestHeader("Authorization") String token){

        AppUser user = userService.findUserProfile();
        userService.updateUser(user.getId(), request);

        UpdateUserResponse response = new UpdateUserResponse(
                "User profile updated successfully",
                true
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
