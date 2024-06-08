package com.whatsapp.Whatsappclone.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String content;

    private LocalDateTime timestamp;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "from_user") // Add JoinColumn annotation
    private AppUser fromUser;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id") // Add JoinColumn annotation
    private AppUser user;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "chat_id") // Add JoinColumn annotation
    private Chat chat;
}
