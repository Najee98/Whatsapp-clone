package com.whatsapp.Whatsappclone.Repositories;

import com.whatsapp.Whatsappclone.Models.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<AppUser, Integer> {

    /**
     * Finds an AppUser by their email (username).
     *
     * @param email the email (username) to search for.
     * @return the AppUser with the given email.
     */
    @Query("select u from AppUser u where u.username = :email")
    AppUser findByEmail(@Param("email") String email);

    /**
     * Searches for users whose full name or username contains the search query,
     * excluding the user with the specified ID.
     *
     * @param searchQuery   the text to search for in full names and usernames.
     * @param requestUserId the ID of the user to exclude from the results.
     * @return a list of users matching the search criteria.
     */
    @Query("select u from AppUser u " +
            "where (u.fullName like %:query% " +
            "or u.username like %:query%) " +
            "and u.id <> :requestUserId")
    List<AppUser> searchUser(@Param("query") String searchQuery,
                             @Param("requestUserId") Integer requestUserId);

    /**
     * Finds an AppUser by their username.
     *
     * @param username the username to search for.
     * @return an Optional containing the AppUser, if found.
     */
    Optional<AppUser> findByUsername(String username);

    /**
     * Finds users in a chat excluding the specified user.
     *
     * @param chatId the ID of the chat.
     * @param user   the user to exclude from the results.
     * @return a list of users in the chat excluding the specified user.
     */
    @Query("select c.users from Chat c " +
            "join c.users u " +
            "where c.id = :chatId and u <> :user")
    List<AppUser> findChatTargetUser(@Param("chatId") Integer chatId, @Param("user") AppUser user);
}
