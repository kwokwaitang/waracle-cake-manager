package com.waracle.cake_manager.controller;

import com.waracle.cake_manager.dto.CakeDto;
import com.waracle.cake_manager.form.NewCakeDetails;
import com.waracle.cake_manager.pojo.NewCakeRequest;
import com.waracle.cake_manager.pojo.NewCakeResponse;
import com.waracle.cake_manager.service.CakeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.iterableWithSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class CakeControllerTest {

    @Mock
    private Model model;

    @Mock
    private CakeService cakeService;

    private CakeController controllerUnderTest;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        controllerUnderTest = new CakeController(cakeService);
        mvc = standaloneSetup(controllerUnderTest).build();
    }

    @Test
    void index() throws Exception {
        CakeDto cakeDto1 = new CakeDto();
        cakeDto1.setEmployeeId(1L);
        cakeDto1.setTitle("The Biscoff Cake");
        cakeDto1.setDescription("Vanilla sponge topped with Lotus biscuits");
        cakeDto1.setImage("https://cdn.shopify.com/s/files/1/0490/6418/1918/products/DD_Lotus_Cake_Full-scaled-1.jpg?v=1602446203");

        List<CakeDto> cakes = new ArrayList<>(Arrays.asList(cakeDto1));

        when(cakeService.getAvailableCakes()).thenReturn(cakes);

        mvc.perform(get("/"))
                .andExpect(model().attribute("cakes", iterableWithSize((equalTo(1)))))
                .andExpect(status().isOk())
                .andExpect(view().name("index")).andDo(print());
    }

    @Test
    void captureNewCakeDetails() throws Exception {
        mvc.perform(get("/new-cake-details"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"));
    }

    @Test
    void onSubmit_withNoErrorsSuccessfullyAddedCake() throws Exception {
        when(cakeService.getNewCakeRequest(any(NewCakeDetails.class))).thenReturn(new NewCakeRequest());
        when(cakeService.addCake(any(NewCakeRequest.class))).thenReturn(new NewCakeResponse(88L));

        mvc.perform(post("/new-cake-details")
                .param("title", "The Biscoff Cake")
                .param("description", "Vanilla sponge topped with Lotus biscuits")
                .param("image", "https://cdn.shopify.com/s/files/1/0490/6418/1918/products/DD_Lotus_Cake_Full-scaled-1.jpg?v=1602446203"))
                .andExpect(status().isOk())
                .andExpect(view().name("successfully-added-cake"));
    }

    @Test
    void onSubmit_withErrors_missingTitle() throws Exception {
        String[] erroneousFields = { "title" };

        mvc.perform(post("/new-cake-details")
                        .param("title", "")
                        .param("description", "Vanilla sponge topped with Lotus biscuits")
                        .param("image", "https://cdn.shopify.com/s/files/1/0490/6418/1918/products/DD_Lotus_Cake_Full-scaled-1.jpg?v=1602446203"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeErrorCount("newCakeDetails", erroneousFields.length))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "title", "error.title.missing"))
                .andDo(print());
    }

    @Test
    void onSubmit_withErrors_missingDescription() throws Exception {
        String[] erroneousFields = { "description" };

        mvc.perform(post("/new-cake-details")
                        .param("title", "The Biscoff Cake")
                        .param("description", "")
                        .param("image", "https://cdn.shopify.com/s/files/1/0490/6418/1918/products/DD_Lotus_Cake_Full-scaled-1.jpg?v=1602446203"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeErrorCount("newCakeDetails", erroneousFields.length))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "description", "error.description.missing"))
                .andDo(print());
    }

    @Test
    void onSubmit_withErrors_missingImage() throws Exception {
        String[] erroneousFields = { "image" };

        mvc.perform(post("/new-cake-details")
                        .param("title", "The Biscoff Cake")
                        .param("description", "Vanilla sponge topped with Lotus biscuits")
                        .param("image", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeErrorCount("newCakeDetails", erroneousFields.length))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "image", "error.image.missing"))
                .andDo(print());
    }

    @Test
    void onSubmit_withErrors_missingHttpOrHttps() throws Exception {
        String[] erroneousFields = { "image" };

        mvc.perform(post("/new-cake-details")
                        .param("title", "The Biscoff Cake")
                        .param("description", "Vanilla sponge topped with Lotus biscuits")
                        .param("image", "cdn.shopify.com/s/files/1/0490/6418/1918/products/DD_Lotus_Cake_Full-scaled-1.jpg?v=1602446203"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeErrorCount("newCakeDetails", erroneousFields.length))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "image", "error.image.wrong-format"))
                .andDo(print());
    }

    @Test
    void onSubmit_withErrors_missingTitleAndDescription() throws Exception {
        String[] erroneousFields = { "title", "description" };

        mvc.perform(post("/new-cake-details")
                        .param("title", "")
                        .param("description", "")
                        .param("image", "https://cdn.shopify.com/s/files/1/0490/6418/1918/products/DD_Lotus_Cake_Full-scaled-1.jpg?v=1602446203"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeErrorCount("newCakeDetails", erroneousFields.length))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "title", "error.title.missing"))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "description", "error.description.missing"))
                .andDo(print());
    }

    @Test
    void onSubmit_withErrors_missingTitleDescriptionAndImage() throws Exception {
        String[] erroneousFields = { "title", "description", "image" };

        mvc.perform(post("/new-cake-details")
                        .param("title", "")
                        .param("description", "")
                        .param("image", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeErrorCount("newCakeDetails", erroneousFields.length))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "title", "error.title.missing"))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "description", "error.description.missing"))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "image", "error.image.missing"))
                .andDo(print());
    }

    @Test
    void onSubmit_withErrors_missingTitleDescriptionAndImageMissingHttpOrHttps() throws Exception {
        String[] erroneousFields = { "title", "description", "image" };

        mvc.perform(post("/new-cake-details")
                        .param("title", "")
                        .param("description", "")
                        .param("image", "cdn.shopify.com/s/files/1/0490/6418/1918/products/DD_Lotus_Cake_Full-scaled-1.jpg?v=1602446203"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeErrorCount("newCakeDetails", erroneousFields.length))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "title", "error.title.missing"))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "description", "error.description.missing"))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "image", "error.image.wrong-format"))
                .andDo(print());
    }
}