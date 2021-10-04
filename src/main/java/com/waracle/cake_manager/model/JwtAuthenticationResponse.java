package com.waracle.cake_manager.model;

import java.io.Serializable;
import java.util.Objects;

public class JwtAuthenticationResponse implements Serializable {

    private String jwtToken;

    public JwtAuthenticationResponse() {
    }

    public JwtAuthenticationResponse(String jwtToken) {
        this.jwtToken = Objects.requireNonNull(jwtToken, () -> "Missing a JWT token");
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
