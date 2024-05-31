package com.whatsapp.Whatsappclone.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fullName;
    private String email;
    private String profilePicture;
    private String password;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppUser appUser = (AppUser) o;
        return Objects.equals(id, appUser.id) &&
                Objects.equals(fullName, appUser.fullName) &&
                Objects.equals(email, appUser.email) &&
                Objects.equals(profilePicture, appUser.profilePicture) &&
                Objects.equals(password, appUser.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, email, profilePicture, password);
    }
}
