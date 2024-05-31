package com.whatsapp.Whatsappclone.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest implements Serializable {

    private String fullName;
    private String profilePicture;

}
