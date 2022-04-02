package com.waracle.cake_manager.v1update;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class TestControllerTest {

    TestRestTemplate testRestTemplate;

    @Disabled("Not set-up as yet")
    @Test
    void shouldV1Update() {
        testRestTemplate = new TestRestTemplate();

        var body = new Body();
        body.setBody("" +
                "{\n" +
                "\t\"userId\": 1,\n" +
                "\t\"id\": 1,\n" +
                "\t\"title\": \"delectus aut autem\",\n" +
                "\t\"completed\": false\n" +
                "}");

        var response = testRestTemplate.postForEntity("https://dev.localhost.com:8443/v1/update", body, Body.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.CREATED));
    }
}