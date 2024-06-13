package com.whatsapp.Whatsappclone.Repositories;

import com.whatsapp.Whatsappclone.Models.AppUser;
import com.whatsapp.Whatsappclone.Models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for accessing and managing Chat entities in the database.
 */
@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    /**
     * Finds a single chat between two users.
     *
     * @param requestUser The user making the request.
     * @param targetUser  The user being targeted in the chat.
     * @return The Chat entity if found, otherwise null.
     */
    @Query("select c from Chat c where c.isGroup = false " +
            "and :requestUser member of c.users " +
            "and :targetUser member of c.users")
    Chat findChatByUsers(@Param("requestUser") AppUser requestUser,
                         @Param("targetUser") AppUser targetUser);

    /**
     * Finds all chats for a given user, returning a list of selected chat data.
     *
     * @param user The user whose chats are being retrieved.
     * @return A list of objects containing chat data.
     */
    @Query("select c.id, c.name, c.image, c.isGroup, c.createdAt from Chat c where :user member of c.users")
    List<Object[]> findChatDataByUserId(@Param("user") AppUser user);

    /**
     * Retrieves detailed information about a chat, including its users.
     *
     * @param chatId The ID of the chat being retrieved.
     * @return The Chat entity with detailed information.
     */
    @Query("select c from Chat c join fetch c.users " +
            "where c.id = :chatId")
    Chat findChatDetails(@Param("chatId") Integer chatId);
}
