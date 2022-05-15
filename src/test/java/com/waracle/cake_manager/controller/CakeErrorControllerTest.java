package com.waracle.cake_manager.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class CakeErrorControllerTest {

    @Mock
    HttpServletRequest request;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        CakeErrorController controllerUnderTest = new CakeErrorController();
        mvc = standaloneSetup(controllerUnderTest).build();
    }

    @Test
    void shouldDeli8verAnErrorWhenHttpStatusIs500() throws Exception {
        // Given...
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(500);

        // When...
        mvc.perform(get("/error").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("errors/error"))
                .andDo(print());

        // Then...
        //verify(request).getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    }

    @Test
    void shouldDeli8verAnErrorWhenHttpStatusIs404() throws Exception {
        // Given...
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(404);

        // When...
        mvc.perform(get("/error").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("errors/error-404"))
                .andDo(print());

        // Then...
        //verify(request).getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
    }

    @Test
    void shouldExceptionHandling() throws Exception {
        // Given...
        //when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn(500);

        // When...
        mvc.perform(get("/throw-my-own-exception").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(view().name("errors/error-500"))
                .andDo(print());
        // Then...
    }

    @Test
    void shouldMyOwnExceptionHandling() throws Exception {
        // Given...
        // When...
        // Then...
    }

    @Test
    void shouldAnotherMyOwnExceptionHandling() throws Exception {
        // Given...
        // When...
        // Then...
    }
}