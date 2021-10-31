package com.waracle.cake_manager.controller;

import com.waracle.cake_manager.dto.CakeDto;
import com.waracle.cake_manager.form.NewCakeDetails;
import com.waracle.cake_manager.pojo.NewCakeRequest;
import com.waracle.cake_manager.pojo.NewCakeResponse;
import com.waracle.cake_manager.service.CakeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
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

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        CakeController controllerUnderTest = new CakeController(cakeService);
        mvc = standaloneSetup(controllerUnderTest).build();
    }

    @Test
    @DisplayName("When a cake service is unavailable")
    void constructorWithMissingCakeService() {
        Exception exception = Assertions.assertThrows(NullPointerException.class, () -> {
            new CakeController(null);
        });

        String expectedMessage = "Missing a cake service";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void index() throws Exception {
        CakeDto cakeDto1 = new CakeDto();
        cakeDto1.setEmployeeId(1L);
        cakeDto1.setTitle("The Biscoff Cake");
        cakeDto1.setDescription("Vanilla sponge topped with Lotus biscuits");
        cakeDto1.setImage("https://cdn.shopify.com/s/files/1/0490/6418/1918/products/DD_Lotus_Cake_Full-scaled-1" +
                ".jpg?v=1602446203");

        List<CakeDto> cakes = new ArrayList<>(Arrays.asList(cakeDto1));

        when(cakeService.getAvailableCakesViaRestApi()).thenReturn(cakes);

        mvc.perform(get("/"))
                .andExpect(model().attribute("cakes", iterableWithSize((equalTo(1)))))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andDo(print());

        verify(cakeService, times(1)).getAvailableCakesViaRestApi();
    }

    @Test
    @DisplayName("Display only carrot cake recipes")
    void carrotCakeOnly() throws Exception {
        CakeDto carrotCake1 = new CakeDto("abc", "def", "https://abcdef.com");
        CakeDto carrotCake2 = new CakeDto("ghi", "jkl", "https://ghijkl.com");

        List<CakeDto> carrotCakes = new ArrayList<>(Arrays.asList(carrotCake1, carrotCake2));

        // Traditional Mockito style...
        when(cakeService.getCarrotCakes()).thenReturn(carrotCakes);

        // or BDD style...
        given(cakeService.getCarrotCakes()).willReturn(carrotCakes);

        mvc.perform(get("/carrot-cakes"))
                .andExpect(model().attribute("cakes", iterableWithSize((equalTo(2)))))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andDo(print());

        // Traditional Mockito style...
        verify(cakeService, times(1)).getCarrotCakes();

        // or BDD style...
        then(cakeService).should().getCarrotCakes();
    }

    @Test
    @DisplayName("Display form to capture cake details")
    void captureNewCakeDetails() throws Exception {
        mvc.perform(get("/new-cake-details"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"));
    }

    @Test
    @DisplayName("Submit all required cake details")
    void onSubmit_withNoErrors_SuccessfullyAddedCake() throws Exception {
        when(cakeService.getNewCakeRequest(any(NewCakeDetails.class))).thenReturn(new NewCakeRequest());
        when(cakeService.addCakeViaRestApi(any(NewCakeRequest.class))).thenReturn(new NewCakeResponse(88L));

        mvc.perform(post("/new-cake-details")
                        .param("title", "The Biscoff Cake")
                        .param("description", "Vanilla sponge topped with Lotus biscuits")
                        .param("image", "https://cdn.shopify.com/s/files/1/0490/6418/1918/products/DD_Lotus_Cake_Full" +
                                "-scaled-1.jpg?v=1602446203"))
                .andExpect(status().isOk())
                .andExpect(view().name("successfully-added-cake"));

        verify(cakeService, times(1)).getNewCakeRequest(any(NewCakeDetails.class));
        verify(cakeService, times(1)).addCakeViaRestApi(any(NewCakeRequest.class));
    }

    @Test
    void onSubmit_withNoErrors_UnsuccessfullyAddedCake() throws Exception {
        when(cakeService.getNewCakeRequest(any(NewCakeDetails.class))).thenReturn(new NewCakeRequest());
        when(cakeService.addCakeViaRestApi(any(NewCakeRequest.class))).thenReturn(new NewCakeResponse(null));

        mvc.perform(post("/new-cake-details")
                        .param("title", "The Biscoff Cake")
                        .param("description", "Vanilla sponge topped with Lotus biscuits")
                        .param("image", "https://cdn.shopify.com/s/files/1/0490/6418/1918/products/DD_Lotus_Cake_Full" +
                                "-scaled-1.jpg?v=1602446203"))
                .andExpect(status().isOk())
                .andExpect(view().name("unsuccessfully-added-cake"));

        verify(cakeService, times(1)).getNewCakeRequest(any(NewCakeDetails.class));
        verify(cakeService, times(1)).addCakeViaRestApi(any(NewCakeRequest.class));
    }

    @Test
    @DisplayName("Submit cake details but missing the title")
    void onSubmit_withErrors_missingTitle() throws Exception {
        String[] erroneousFields = {"title"};

        mvc.perform(post("/new-cake-details")
                        .param("title", "")
                        .param("description", "Vanilla sponge topped with Lotus biscuits")
                        .param("image", "https://cdn.shopify.com/s/files/1/0490/6418/1918/products/DD_Lotus_Cake_Full" +
                                "-scaled-1.jpg?v=1602446203"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeErrorCount("newCakeDetails", erroneousFields.length))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "title", "error.title.missing"))
                .andDo(print());
    }

    @Test
    void onSubmit_withErrors_missingDescription() throws Exception {
        String[] erroneousFields = {"description"};

        mvc.perform(post("/new-cake-details")
                        .param("title", "The Biscoff Cake")
                        .param("description", "")
                        .param("image", "https://cdn.shopify.com/s/files/1/0490/6418/1918/products/DD_Lotus_Cake_Full" +
                                "-scaled-1.jpg?v=1602446203"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeErrorCount("newCakeDetails", erroneousFields.length))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "description", "error.description" +
                        ".missing"))
                .andDo(print());
    }

    @Test
    void onSubmit_withErrors_missingImage() throws Exception {
        String[] erroneousFields = {"image"};

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
        String[] erroneousFields = {"image"};

        mvc.perform(post("/new-cake-details")
                        .param("title", "The Biscoff Cake")
                        .param("description", "Vanilla sponge topped with Lotus biscuits")
                        .param("image", "cdn.shopify.com/s/files/1/0490/6418/1918/products/DD_Lotus_Cake_Full-scaled" +
                                "-1.jpg?v=1602446203"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeErrorCount("newCakeDetails", erroneousFields.length))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "image", "error.image.wrong-format"))
                .andDo(print());
    }

    @Test
    void onSubmit_withErrors_missingTitleAndDescription() throws Exception {
        String[] erroneousFields = {"title", "description"};

        mvc.perform(post("/new-cake-details")
                        .param("title", "")
                        .param("description", "")
                        .param("image", "https://cdn.shopify.com/s/files/1/0490/6418/1918/products/DD_Lotus_Cake_Full" +
                                "-scaled-1.jpg?v=1602446203"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeErrorCount("newCakeDetails", erroneousFields.length))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "title", "error.title.missing"))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "description", "error.description" +
                        ".missing"))
                .andDo(print());
    }

    @Test
    void onSubmit_withErrors_missingTitleDescriptionAndImage() throws Exception {
        String[] erroneousFields = {"title", "description", "image"};

        mvc.perform(post("/new-cake-details")
                        .param("title", "")
                        .param("description", "")
                        .param("image", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeErrorCount("newCakeDetails", erroneousFields.length))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "title", "error.title.missing"))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "description", "error.description" +
                        ".missing"))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "image", "error.image.missing"))
                .andDo(print());
    }

    @Test
    void onSubmit_withErrors_missingTitleDescriptionAndImageMissingHttpOrHttps() throws Exception {
        String[] erroneousFields = {"title", "description", "image"};

        mvc.perform(post("/new-cake-details")
                        .param("title", "")
                        .param("description", "")
                        .param("image", "cdn.shopify.com/s/files/1/0490/6418/1918/products/DD_Lotus_Cake_Full-scaled" +
                                "-1.jpg?v=1602446203"))
                .andExpect(status().isOk())
                .andExpect(view().name("form"))
                .andExpect(model().attributeErrorCount("newCakeDetails", erroneousFields.length))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "title", "error.title.missing"))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "description", "error.description" +
                        ".missing"))
                .andExpect(model().attributeHasFieldErrorCode("newCakeDetails", "image", "error.image.wrong-format"))
                .andDo(print());
    }
}