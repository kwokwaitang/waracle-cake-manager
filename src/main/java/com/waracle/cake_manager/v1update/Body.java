package com.waracle.cake_manager.v1update;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Body {

    @Pattern(regexp = "^.*$")
    @Size(min = 10, max = 1024)
    private String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
