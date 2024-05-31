package com.whatsapp.Whatsappclone.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chats")
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String image;

    @Column(name = "is_group")
    private boolean isGroup;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private AppUser createdBy;

    @ManyToMany
    private Set<AppUser> users = new HashSet<>();

    @ManyToMany
    private Set<AppUser> admins = new HashSet<>();

    @OneToMany
    private List<Message> messages = new ArrayList<>();
}
