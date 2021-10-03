package com.waracle.cake_manager.model;

public class NewCakeResponse {
    private Long id;

    public NewCakeResponse() {
    }

    public NewCakeResponse(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "NewCakeResponse{" +
                "id=" + id +
                '}';
    }
}
