package com.whatsapp.Whatsappclone.Repositories;

import com.whatsapp.Whatsappclone.Dto.ChatDetailsDto;
import com.whatsapp.Whatsappclone.Dto.ChatsIndexDto;
import com.whatsapp.Whatsappclone.Models.AppUser;
import com.whatsapp.Whatsappclone.Models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

//    @Query("select distinct c from Chat c join c.users u " +
//            "left join c.admins a where u.id = :userId " +
//            "or a.id = :userId")
//    public List<Chat> findChatByUserId(@Param("userId") Integer userId);


    @Query("select c from Chat c where c.isGroup = false " +
            "and :requestUser member of c.users " +
            "and :targetUser member of c.users")
    Chat findChatByUsers(@Param("requestUser") AppUser requestUser,
                                @Param("targetUser") AppUser targetUser);


//    @Query("select new com.whatsapp.Whatsappclone.Dto.ChatsIndexDto(" +
//            "c.id," +
//            "c.name," +
//            "c.image," +
//            "c.isGroup," +
//            " 'last message test') " +
//            "from Chat c " +
//            "where c.users = :user ")
//    List<ChatsIndexDto> findChatByUserId(@Param("user") AppUser user);

    @Query("select c.id, c.name, c.image, c.isGroup, c.createdAt from Chat c where :user member of c.users")
    List<Object[]> findChatDataByUserId(@Param("user") AppUser user);

//    @Query("select new com.whatsapp.Whatsappclone.Dto.ChatDetailsDto(" +
//            "c.id," +
//            "c.name," +
//            "c.image) " +
//            "from Chat c " +
//            "where c.id = :chatId")
    @Query("select c from Chat c join fetch c.users " +
            "where c.id = :chatId")
    Chat findChatDetails(@Param("chatId") Integer chatId);
}
