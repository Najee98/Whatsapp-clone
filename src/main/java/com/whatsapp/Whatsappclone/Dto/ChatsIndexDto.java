package com.whatsapp.Whatsappclone.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatsIndexDto implements Serializable {

    Integer id;
    String name;
    String image;
    boolean group;
    String lastMessage;

}
