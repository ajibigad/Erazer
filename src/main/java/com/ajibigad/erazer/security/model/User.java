package com.ajibigad.erazer.security.model;

import com.ajibigad.erazer.model.FcmToken;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ajibigad on 13/08/2017.
 */
@Entity
public class User {

    @Id
    @NotNull
    private
    String username;

    @NotNull
    @Column(nullable = false)
    private String firstname;

    @NotNull
    @Column(nullable = false)
    private String lastname;

    @NotNull
    @Email
    @Column(unique = true, nullable = false)
    private
    String email;

    @NotNull
    @Size(min = 8, message = "password lenght must not be less than 8")
    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(
                    name = "username", referencedColumnName = "username"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>(0);

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private Set<FcmToken> fcmTokens = new HashSet<>(0);

    @JsonIgnore
    private String fcmNotificationKey;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Set<FcmToken> getFcmTokens() {
        return fcmTokens;
    }

    public void setFcmTokens(Set<FcmToken> fcmTokens) {
        this.fcmTokens = fcmTokens;
    }

    public String getFcmNotificationKey() {
        return fcmNotificationKey;
    }

    public void setFcmNotificationKey(String fcmNotificationKey) {
        this.fcmNotificationKey = fcmNotificationKey;
    }
}
