package com.waracle.cake_manager.model;

import java.io.Serializable;
import java.util.Objects;

public class JwtAuthenticationRequest implements Serializable {

    private String username;
    private String password;

    public JwtAuthenticationRequest() {
    }

    public JwtAuthenticationRequest(String username, String password) {
        this.username = Objects.requireNonNull(username, () -> "Missing a username");
        this.password = Objects.requireNonNull(password, () -> "Missing a password");
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
