package com.waracle.cake_manager.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class NewCakeRequestDto {
    private String title;

    private String description;

    private String imageUrl;

    public String getTitle() {
        return title;
    }

    public NewCakeRequestDto() {
    }

    public NewCakeRequestDto(String title, String description, String imageUrl) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "NewCakeRequest{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
