package com.waracle.cake_manager.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Column;

public class CakeDto {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long employeeId;

    private String title;

    @JsonAlias("desc")
    private String description;

    private String image;

    public CakeDto() {
    }

    public CakeDto(String title, String description, String image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public CakeDto(Long employeeId, String title, String description, String image) {
        this.employeeId = employeeId;
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getTitle() {
        return title;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "CakeDto{" +
                "employeeId=" + employeeId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
