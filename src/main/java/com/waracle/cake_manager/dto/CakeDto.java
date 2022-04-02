package com.waracle.cake_manager.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CakeDto implements Comparable<CakeDto> {

    @JsonProperty("employeeId")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long employeeId;

    @JsonProperty("title")
    @Pattern(regexp = "^.*$")
    @Size(min = 10, max = 1024)
    private String title;

    @JsonProperty("description")
    @JsonAlias("desc")
    @ApiModelProperty(value = "Description", dataType = "String", example = "Whatever...")
    private String description;

    // https://stackoverflow.com/questions/49032676/swagger-apimodelproperty-example-value-null-for-long
    @JsonProperty("image")
    //@ApiModelProperty(value = "ABC", dataType = "String", example = "null")
    @Pattern(regexp = "^.*$")
    @Size(min = 20, max = 2048)
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

    @Override
    public int compareTo(CakeDto that) {
        return Long.compare(that.employeeId, this.employeeId);
    }
}
