package com.whatsapp.Whatsappclone.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessagesDto implements Serializable {

    Integer id;
    String content;
    LocalDateTime timestamp;

    Integer chatId;
    Integer userId;

}
