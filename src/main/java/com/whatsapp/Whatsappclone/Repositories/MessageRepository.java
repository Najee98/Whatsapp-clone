package com.whatsapp.Whatsappclone.Repositories;

import com.whatsapp.Whatsappclone.Dto.ChatMessagesDto;
import com.whatsapp.Whatsappclone.Models.AppUser;
import com.whatsapp.Whatsappclone.Models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Query("select m from Message m " +
            "join m.chat c " +
            "where c.id = :chatId")
    public List<Message> findByChatId(@Param("chatId") Integer chatId);

    @Query("select new com.whatsapp.Whatsappclone.Dto.ChatMessagesDto(" +
            "m.id," +
            "m.content," +
            "m.timestamp," +
            "c.id," +
            "u.id," +
            "u.fullName," +
            "u.profilePicture) " +
            "from Message m join m.chat c " +
            "join m.user u " +
            "where :user1 member of c.users " +
            "and :user2 member of c.users " +
            "and m.chat.id = :chatId")
    List<ChatMessagesDto> findChatMessages(@Param("chatId") Integer chatId,
                                           AppUser user1,
                                           AppUser user2);
}
