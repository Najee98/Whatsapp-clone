package com.whatsapp.Whatsappclone.Repositories;

import com.whatsapp.Whatsappclone.Models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Integer> {

    @Query("select u from AppUser u where u.email = :email ")
    AppUser findByEmail(@Param("email") String email);

    @Query("select u from AppUser u " +
            "where u.fullName like %:query% " +
            "or u.email like %:query% ")
    List<AppUser> searchUser(@Param("query") String searchQuery);

    @Query("select u from AppUser u where u.email = :username")
    AppUser findUserByUsername(@Param("username") String requestUserName);

    @Query("select c.users from Chat c " +
            "join c.users u " +
            "where c.id = :chatId and u <> :user")
    List<AppUser> findChatTargetUser(@Param("chatId") Integer chatId, @Param("user") AppUser user);
}
