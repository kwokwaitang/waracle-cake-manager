package com.waracle.cake_manager.dto;

public class NewCakeResponseDto {
    private Long id;

    public NewCakeResponseDto() {
    }

    public NewCakeResponseDto(Long id) {
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
