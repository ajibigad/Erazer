package com.ajibigad.erazer.model;

import com.ajibigad.erazer.security.model.User;

import javax.persistence.*;

/**
 * Created by ajibigad on 15/08/2017.
 */
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"token", "user_id"}))
public class FcmToken {

    @Id
    private String token;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
