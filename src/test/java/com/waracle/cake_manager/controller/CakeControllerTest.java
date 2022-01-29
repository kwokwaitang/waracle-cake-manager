package com.waracle.cake_manager.controller;

import com.waracle.cake_manager.dto.CakeDto;
import com.waracle.cake_manager.dto.NewCakeRequestDto;
import com.waracle.cake_manager.dto.NewCakeResponseDto;
import com.waracle.cake_manager.form.NewCakeDetails;
import com.waracle.cake_manager.service.CakeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class CakeControllerTest {

    @Mock
    private CakeService cakeService;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        CakeController controllerUnderTest = new CakeController(cakeService);
        mvc = standaloneSetup(controllerUnderTest).build();
    }

    @Disabled("Not too sure why this needs to be disabled - to look into later")
    @Test
    void index() throws Exception {
        CakeDto cakeDto1 = new CakeDto();
        cakeDto1.setEmployeeId(1L);
        cakeDto1.setTitle("The Biscoff Cake");
        cakeDto1.setDescription("Vanilla sponge topped with Lotus biscuits");
        cakeDto1.setImage("https://cdn.shopify.com/s/files/1/0490/6418/1918/products/DD_Lotus_Cake_Full-scaled-1.jpg?v=1602446203");

        List<CakeDto> cakes = List.of(cakeDto1);

        when(cakeService.getAvailableCakes()).thenReturn(cakes);

        mvc.perform(get("/"))
                .andExpect(model().attribute("cakes", iterableWithSize((equalTo(1)))))
                .andExpect(status().isOk())
                .andExpect(view().name("index")).andDo(print());

        verify(cakeService, times(1)).getAvailableCakes();
    }

    @Test
    void captureNewCakeDetails() throws Exception {
        mvc.perform(get("/new-cake-details"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"));
    }

    @Test
    void onSubmit_withNoErrorsSuccessfullyAddedCake() throws Exception {
        when(cakeService.getNewCakeRequestDto(any(NewCakeDetails.class))).thenReturn(new NewCakeRequestDto());
        when(cakeService.addCake(any(NewCakeRequestDto.class))).thenReturn(new NewCakeResponseDto(88L));

        mvc.perform(post("/new-cake-details")
                .param("title", "The Biscoff Cake")
                .param("description", "Vanilla sponge topped with Lotus biscuits")
                .param("image", "https://cdn.shopify.com/s/files/1/0490/6418/1918/products/DD_Lotus_Cake_Full-scaled-1.jpg?v=1602446203"))
                .andExpect(status().isOk())
                .andExpect(view().name("successfully-added-cake"));

        verify(cakeService, times(1)).getNewCakeRequestDto(any(NewCakeDetails.class));
        verify(cakeService, times(1)).addCake(any(NewCakeRequestDto.class));
    }

    @Test
    void onSubmit_withErrors_missingTitle() throws Exception {
        mvc.perform(post("/new-cake-details")
                        .param("title", "")
                        .param("description", "Vanilla sponge topped with Lotus biscuits")
                        .param("image", "https://cdn.shopify.com/s/files/1/0490/6418/1918/products/DD_Lotus_Cake_Full-scaled-1.jpg?v=1602446203"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeErrorCount("newCakeDetails", new String[] { "title" }.length))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "title", "error.title.missing"))
                .andDo(print());
    }

    @Test
    void onSubmit_withErrors_missingDescription() throws Exception {
        mvc.perform(post("/new-cake-details")
                        .param("title", "The Biscoff Cake")
                        .param("description", "")
                        .param("image", "https://cdn.shopify.com/s/files/1/0490/6418/1918/products/DD_Lotus_Cake_Full-scaled-1.jpg?v=1602446203"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeErrorCount("newCakeDetails", new String[] { "description" }.length))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "description", "error.description.missing"))
                .andDo(print());
    }

    @Test
    void onSubmit_withErrors_missingImage() throws Exception {
        mvc.perform(post("/new-cake-details")
                        .param("title", "The Biscoff Cake")
                        .param("description", "Vanilla sponge topped with Lotus biscuits")
                        .param("image", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeErrorCount("newCakeDetails", new String[] { "image" }.length))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "image", "error.image.missing"))
                .andDo(print());
    }

    @Test
    void onSubmit_withErrors_missingHttpOrHttps() throws Exception {
        mvc.perform(post("/new-cake-details")
                        .param("title", "The Biscoff Cake")
                        .param("description", "Vanilla sponge topped with Lotus biscuits")
                        .param("image", "cdn.shopify.com/s/files/1/0490/6418/1918/products/DD_Lotus_Cake_Full-scaled-1.jpg?v=1602446203"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeErrorCount("newCakeDetails", new String[] { "image" }.length))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "image", "error.image.wrong-format"))
                .andDo(print());
    }

    @Test
    void onSubmit_withErrors_missingTitleAndDescription() throws Exception {
        mvc.perform(post("/new-cake-details")
                        .param("title", "")
                        .param("description", "")
                        .param("image", "https://cdn.shopify.com/s/files/1/0490/6418/1918/products/DD_Lotus_Cake_Full-scaled-1.jpg?v=1602446203"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeErrorCount("newCakeDetails", new String[] { "title", "description" }.length))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "title", "error.title.missing"))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "description", "error.description.missing"))
                .andDo(print());
    }

    @Test
    void onSubmit_withErrors_missingTitleDescriptionAndImage() throws Exception {
        mvc.perform(post("/new-cake-details")
                        .param("title", "")
                        .param("description", "")
                        .param("image", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeErrorCount("newCakeDetails", new String[] { "title", "description", "image" }.length))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "title", "error.title.missing"))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "description", "error.description.missing"))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "image", "error.image.missing"))
                .andDo(print());
    }

    @Test
    void onSubmit_withErrors_missingTitleDescriptionAndImageMissingHttpOrHttps() throws Exception {
        mvc.perform(post("/new-cake-details")
                        .param("title", "")
                        .param("description", "")
                        .param("image", "cdn.shopify.com/s/files/1/0490/6418/1918/products/DD_Lotus_Cake_Full-scaled-1.jpg?v=1602446203"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeErrorCount("newCakeDetails", new String[] { "title", "description", "image" }.length))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "title", "error.title.missing"))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "description", "error.description.missing"))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "image", "error.image.wrong-format"))
                .andDo(print());
    }
}