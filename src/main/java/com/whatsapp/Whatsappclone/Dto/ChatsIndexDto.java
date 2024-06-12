package com.whatsapp.Whatsappclone.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatsIndexDto implements Serializable {

    Integer id;
    String name;
    String image;
    boolean group;
    String lastMessage;
    LocalDateTime lastMessageTimeStamp;
    LocalDateTime createdAt;

}
