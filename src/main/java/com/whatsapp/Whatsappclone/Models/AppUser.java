package com.whatsapp.Whatsappclone.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.whatsapp.Whatsappclone.Security.UserRole;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import jakarta.persistence.*;

import java.util.Collection;
import java.util.List;

/**
 * Represents a user in the application. This class implements UserDetails for Spring Security integration.
 */
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AppUser implements UserDetails {

    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Full name of the user.
     */
    private String fullName;

    /**
     * Username used for authentication.
     */
    private String username;

    /**
     * URL or path to the user's profile picture.
     */
    private String profilePicture;

    /**
     * Password for the user account. It is marked as @JsonIgnore to prevent it from being serialized.
     */
    @JsonIgnore
    private String password;

    /**
     * Role of the user, indicating their permissions.
     */
    @Enumerated(value = EnumType.STRING)
    private UserRole role;

    /**
     * Returns the authorities granted to the user. This is used by Spring Security.
     *
     * @return a collection of authorities (roles) granted to the user.
     */
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    /**
     * Indicates whether the user's account has expired. An expired account cannot be authenticated.
     *
     * @return true if the user's account is non-expired, false otherwise.
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is locked or unlocked. A locked user cannot be authenticated.
     *
     * @return true if the user is not locked, false otherwise.
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials (password) have expired. Expired credentials prevent authentication.
     *
     * @return true if the user's credentials are non-expired, false otherwise.
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user is enabled or disabled. A disabled user cannot be authenticated.
     *
     * @return true if the user is enabled, false otherwise.
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
