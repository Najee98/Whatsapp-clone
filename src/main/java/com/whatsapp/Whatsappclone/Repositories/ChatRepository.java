package com.whatsapp.Whatsappclone.Repositories;

import com.whatsapp.Whatsappclone.Models.AppUser;
import com.whatsapp.Whatsappclone.Models.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Integer> {

    @Query("select distinct c from Chat c join c.users u " +
            "left join c.admins a where u.id = :userId " +
            "or a.id = :userId")
    public List<Chat> findChatByUserId(@Param("userId") Integer userId);


    @Query("select c from Chat c where c.isGroup = false " +
            "and :requestUser member of c.users " +
            "and :targetUser member of c.users")
    public Chat findChatByUsers(@Param("requestUser") AppUser requestUser,
                                @Param("targetUser") AppUser targetUser);
}
