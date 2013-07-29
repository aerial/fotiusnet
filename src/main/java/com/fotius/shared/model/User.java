package com.fotius.shared.model;


import com.google.gwt.user.client.rpc.GwtTransient;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @OneToMany(cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    @GwtTransient
    private List<Message> sentMessages;

    @OneToMany(cascade = {CascadeType.ALL},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "recipient_id")
    @GwtTransient
    private List<Message> receivedMessages;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
