package com.whatsapp.Whatsappclone.Services;

import com.whatsapp.Whatsappclone.Dto.UpdateUserRequest;
import com.whatsapp.Whatsappclone.Exceptions.CustomExceptions.UserException;
import com.whatsapp.Whatsappclone.Models.AppUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserService {

    AppUser findUserById(Integer id);

    AppUser findUserProfile(String jwt);

    AppUser updateUser(Integer id, UpdateUserRequest request) throws UserException;

    List<AppUser> searchUser(String searchQuery);

    AppUser findUserByUsername(String username);
}
