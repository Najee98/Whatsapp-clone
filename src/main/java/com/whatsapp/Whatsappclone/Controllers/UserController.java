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

    @GetMapping("/profile")
    public ResponseEntity<AppUser> getUserProfile(@RequestHeader("Authorization") String token){

        AppUser user = userService.findUserProfile(token);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{searchQuery}")
    public ResponseEntity<List<AppUser>> searchUsers(@PathVariable String searchQuery){
        List<AppUser> users = userService.searchUser(searchQuery);

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<UpdateUserResponse> updateUserProfile(
            @RequestBody UpdateUserRequest request,
            @RequestHeader("Authorization") String token){

        AppUser user = userService.findUserProfile(token);
        userService.updateUser(user.getId(), request);

        UpdateUserResponse response = new UpdateUserResponse(
                "User profile updated successfully",
                true
        );

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
