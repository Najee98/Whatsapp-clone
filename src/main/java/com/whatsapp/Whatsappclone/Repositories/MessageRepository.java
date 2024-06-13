package com.whatsapp.Whatsappclone.Repositories;

import com.whatsapp.Whatsappclone.Dto.ChatMessagesDto;
import com.whatsapp.Whatsappclone.Models.AppUser;
import com.whatsapp.Whatsappclone.Models.Chat;
import com.whatsapp.Whatsappclone.Models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    /**
     * Retrieves chat messages for a single chat between two users.
     *
     * @param chatId the ID of the chat.
     * @param user1  the first user in the chat.
     * @param user2  the second user in the chat.
     * @return a list of chat messages as ChatMessagesDto objects.
     */
    @Query("select new com.whatsapp.Whatsappclone.Dto.ChatMessagesDto(" +
            "m.id," +
            "m.content," +
            "m.timestamp," +
            "c.id," +
            "fru.id," +
            "fru.fullName," +
            "fru.profilePicture," +
            "u.id," +
            "u.fullName," +
            "u.profilePicture) " +
            "from Message m join m.chat c " +
            "join m.user u " +
            "join m.fromUser fru " +
            "where :user1 member of c.users " +
            "and :user2 member of c.users " +
            "and m.chat.id = :chatId")
    List<ChatMessagesDto> findChatMessages(@Param("chatId") Integer chatId,
                                           AppUser user1,
                                           AppUser user2);

    /**
     * Retrieves chat messages for a group chat.
     *
     * @param chatId the ID of the chat.
     * @param user1  a user in the group chat.
     * @return a list of chat messages as ChatMessagesDto objects.
     */
    @Query("select new com.whatsapp.Whatsappclone.Dto.ChatMessagesDto(" +
            "m.id," +
            "m.content," +
            "m.timestamp," +
            "c.id," +
            "u.id," +
            "u.fullName," +
            "u.profilePicture," +
            "c.id," +
            "c.name," +
            "c.image) " +
            "from Message m join m.chat c " +
            "join m.fromUser u " +
            "where :user1 member of c.users " +
            "and m.chat.id = :chatId")
    List<ChatMessagesDto> findChatMessagesForGroup(@Param("chatId") Integer chatId, AppUser user1);

    /**
     * Retrieves the last message in a given chat.
     *
     * @param chat the chat to find the last message for.
     * @return the last message in the chat.
     */
    @Query("select m from Message m " +
            "where m.chat = :chat " +
            "order by m.timestamp desc limit 1")
    Message getLastMessageInChat(@Param("chat") Chat chat);

    /**
     * Finds the most recent message in a chat by chat ID.
     *
     * @param chatId the ID of the chat.
     * @return a list of messages with the most recent one at the top.
     */
    List<Message> findTopByChatIdOrderByTimestampDesc(Integer chatId);

}
