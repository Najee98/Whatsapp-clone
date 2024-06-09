package com.whatsapp.Whatsappclone.Models;


import com.whatsapp.Whatsappclone.Security.UserRole;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String fullName;
    private String username;
    private String profilePicture;
    private String password;

    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.Objects;
//
//@Entity
//@Table(name = "users")
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class AppUser {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//
//    private String fullName;
//    private String email;
//    private String profilePicture;
//    @JsonIgnore
//    private String password;
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        AppUser appUser = (AppUser) o;
//        return Objects.equals(id, appUser.id) &&
//                Objects.equals(fullName, appUser.fullName) &&
//                Objects.equals(email, appUser.email) &&
//                Objects.equals(profilePicture, appUser.profilePicture) &&
//                Objects.equals(password, appUser.password);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, fullName, email, profilePicture, password);
//    }
//}
