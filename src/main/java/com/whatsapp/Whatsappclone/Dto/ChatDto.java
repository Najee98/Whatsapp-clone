package com.whatsapp.Whatsappclone.Dto;

import com.whatsapp.Whatsappclone.Models.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto implements Serializable {

    Integer id;
    String name;
    String image;

    List<ChatMessagesDto> chatMessages = new ArrayList<>();

}
