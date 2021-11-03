package com.waracle.cake_manager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.waracle.cake_manager.dto.CakeDto;
import com.waracle.cake_manager.dto.NewCakeRequestDto;
import com.waracle.cake_manager.dto.NewCakeResponseDto;
import com.waracle.cake_manager.service.CakeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class CakeRestApiControllerTest {

    @Mock
    private CakeService cakeService;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        CakeRestApiController controllerUnderTest = new CakeRestApiController(cakeService);
        mvc = standaloneSetup(controllerUnderTest).build();
    }

    @Test
    void getListOfCakes_withOneCake() throws Exception {
        CakeDto cake1 = new CakeDto();
        cake1.setEmployeeId(1L);
        cake1.setTitle("The Biscoff Cake");
        cake1.setDescription("Vanilla sponge topped with Lotus biscuits");
        cake1.setImage("https://cdn.shopify.com/s/files/1/0490/6418/1918/products/DD_Lotus_Cake_Full-scaled-1.jpg?v=1602446203");

        List<CakeDto> cakes = new ArrayList<>(Arrays.asList(cake1));

        when(cakeService.getAvailableCakes()).thenReturn(cakes);

        mvc.perform(get("/cakes").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is(cake1.getTitle())));

        verify(cakeService, times(1)).getAvailableCakes();
    }

    @Test
    void getListOfCakes_withTwoCakes() throws Exception {
        CakeDto cake1 = new CakeDto();
        cake1.setEmployeeId(1L);
        cake1.setTitle("The Biscoff Cake");
        cake1.setDescription("Vanilla sponge topped with Lotus biscuits");
        cake1.setImage("https://cdn.shopify.com/s/files/1/0490/6418/1918/products/DD_Lotus_Cake_Full-scaled-1.jpg?v=1602446203");

        CakeDto cake2 = new CakeDto();
        cake2.setEmployeeId(2L);
        cake2.setTitle("Lemon cheesecake");
        cake2.setDescription("A cheesecake made of lemon");
        cake2.setImage("https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg");

        List<CakeDto> cakes = new ArrayList<>(Arrays.asList(cake1, cake2));

        when(cakeService.getAvailableCakes()).thenReturn(cakes);

        mvc.perform(get("/cakes").contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", is(cake1.getTitle())))
                .andExpect(jsonPath("$[1].title", is("Lemon cheesecake")));

        verify(cakeService, times(1)).getAvailableCakes();
    }

    @Test
    void newCakeDetails() throws Exception {
        NewCakeRequestDto newCakeRequest = new NewCakeRequestDto();
        newCakeRequest.setTitle("Carrot cake");
        newCakeRequest.setDescription("Bugs bunnys favourite");
        newCakeRequest.setImageUrl("http://www.villageinn.com/i/pies/profile/carrotcake_main1.jpg");

        NewCakeResponseDto newCakeResponse = new NewCakeResponseDto();
        newCakeResponse.setId(1L);

        when(cakeService.addCake(any(NewCakeRequestDto.class))).thenReturn(newCakeResponse);

        mvc.perform(post("/cakes").contentType(APPLICATION_JSON).accept(APPLICATION_JSON).content(asJsonString(newCakeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.id").value("1"))
                .andDo(print());

        verify(cakeService, times(1)).addCake(any(NewCakeRequestDto.class));
    }

    /**
     * https://stackoverflow.com/questions/51346781/how-to-test-post-method-in-spring-boot-using-mockito-and-junit
     *
     * @param obj
     * @return
     */
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}