package com.whatsapp.Whatsappclone.Dto;

import com.whatsapp.Whatsappclone.Models.AppUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDetailsDto implements Serializable {

    Integer id;
    String name;
    String image;

    List<AppUser> users = new ArrayList<>();
}
