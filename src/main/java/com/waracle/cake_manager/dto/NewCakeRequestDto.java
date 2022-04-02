package com.waracle.cake_manager.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class NewCakeRequestDto {
    private String title;

    private String description;

    @JsonProperty("imageUrl")
    @Pattern(regexp = "^.*$")
    @Size(min = 3, max = 88)
    private String imageUrl;

    public String getTitle() {
        return title;
    }

    public NewCakeRequestDto() {
    }

    private NewCakeRequestDto(String title, String description, String imageUrl) {
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public static NewCakeRequestDto of(String title, String description, String imageUrl) {
        return new NewCakeRequestDto(title, description, imageUrl);
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
