package com.waracle.cake_manager.service;

import com.waracle.cake_manager.dto.CakeDto;
import com.waracle.cake_manager.form.NewCakeDetails;
import com.waracle.cake_manager.model.Cake;
import com.waracle.cake_manager.pojo.NewCakeRequest;
import com.waracle.cake_manager.pojo.NewCakeResponse;
import com.waracle.cake_manager.repository.CakeRepository;
import org.apache.http.client.HttpClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class CakeServiceImplTest {

    @Mock
    ModelMapper modelMapper = new ModelMapper();

    @Mock
    HttpClient httpClient;

    @Mock
    CakeRepository cakeRepository;

    CakeServiceImpl serviceImplUnderTest;

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        serviceImplUnderTest = new CakeServiceImpl(modelMapper, httpClient, cakeRepository);
    }

    @Test
    void getAvailableCakes_withCakes() {
        // Set-up some entities
        Cake cake1 = new Cake();
        cake1.setEmployeeId(1L);
        cake1.setTitle("Lemon cheesecake");
        cake1.setDescription("A cheesecake made of lemon");
        cake1.setImageUrl("https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg");

        Cake cake2 = new Cake();
        cake2.setEmployeeId(2L);
        cake2.setTitle("victoria sponge");
        cake2.setDescription("sponge with jam");
        cake2.setImageUrl("http://www.bbcgoodfood.com/sites/bbcgoodfood.com/files/recipe_images/recipe-image-legacy-id--1001468_10.jpg");

        // Set-up some Dtos
        CakeDto cakeDto1 = new CakeDto();
        cakeDto1.setTitle("Lemon cheesecake");
        cakeDto1.setDescription("A cheesecake made of lemon");
        cakeDto1.setImage("https://s3-eu-west-1.amazonaws.com/s3.mediafileserver.co.uk/carnation/WebFiles/RecipeImages/lemoncheesecake_lg.jpg");

        CakeDto cakeDto2 = new CakeDto();
        cakeDto2.setTitle("victoria sponge");
        cakeDto2.setDescription("sponge with jam");
        cakeDto2.setImage("http://www.bbcgoodfood.com/sites/bbcgoodfood.com/files/recipe_images/recipe-image-legacy-id--1001468_10.jpg");

        Iterable<Cake> iterable = Arrays.asList(cake1, cake2);

        when(cakeRepository.findAll()).thenReturn(iterable);
        when(modelMapper.map(any(Cake.class), eq(CakeDto.class))).thenReturn(cakeDto1, cakeDto2);

        List<CakeDto> cakeDtos = serviceImplUnderTest.getAvailableCakes();

        assertFalse(cakeDtos.isEmpty());
        assertEquals(2, cakeDtos.size());
        assertEquals(cake1.getTitle(), cakeDtos.get(0).getTitle());
        assertEquals(cake2.getTitle(), cakeDtos.get(1).getTitle());
    }

    @Test
    void getAvailableCakes_withNoCakes() {
        when(cakeRepository.findAll()).thenReturn(Collections.emptyList());

        List<CakeDto> cakeDtos = serviceImplUnderTest.getAvailableCakes();
        assertTrue(cakeDtos.isEmpty());
    }

    @Test
    void addCake() {
        NewCakeRequest newCakeRequest = new NewCakeRequest();
        newCakeRequest.setTitle("abc");
        newCakeRequest.setDescription("def");
        newCakeRequest.setImageUrl("http://ghi.com");

        Cake cake = new Cake();
        cake.setEmployeeId(21L);
        cake.setTitle("abc");
        cake.setDescription("def");
        cake.setImageUrl("http://ghi.com");

        when(modelMapper.map(any(NewCakeRequest.class), eq(Cake.class))).thenReturn(cake);
        when(cakeRepository.save(any(Cake.class))).thenReturn(cake);

        NewCakeResponse newCakeResponse = serviceImplUnderTest.addCake(newCakeRequest);

        assertEquals(21L, newCakeResponse.getId());
    }

    @Test
    void getNewCakeRequest_withNewCakeDetails() {
        NewCakeDetails newCakeDetails = new NewCakeDetails();
        newCakeDetails.setTitle("abc");
        newCakeDetails.setDescription("def");
        newCakeDetails.setImage("https://some-nice-cake.com/with-lots-of-cream.gif");

        NewCakeRequest mewNewCakeRequest = serviceImplUnderTest.getNewCakeRequest(newCakeDetails);

        assertEquals(newCakeDetails.getTitle(), mewNewCakeRequest.getTitle());
        assertEquals(newCakeDetails.getDescription(), mewNewCakeRequest.getDescription());
        assertEquals(newCakeDetails.getImage(), mewNewCakeRequest.getImageUrl());
    }

    @Test
    void getNewCakeRequest_withTitleDescImage() {
        NewCakeRequest mewNewCakeRequest = serviceImplUnderTest.getNewCakeRequest("abc", "def", "https://some-nice" +
                "-cake.com/with-lots-of-cream.gif");

        assertEquals("abc", mewNewCakeRequest.getTitle());
        assertEquals("def", mewNewCakeRequest.getDescription());
        assertEquals("https://some-nice-cake.com/with-lots-of-cream.gif", mewNewCakeRequest.getImageUrl());
    }
}