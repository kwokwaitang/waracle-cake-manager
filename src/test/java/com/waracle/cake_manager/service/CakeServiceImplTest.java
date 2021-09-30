package com.waracle.cake_manager.service;

import com.waracle.cake_manager.dto.CakeDto;
import com.waracle.cake_manager.model.Cake;
import com.waracle.cake_manager.repository.CakeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

class CakeServiceImplTest {

    @Autowired
    ModelMapper modelMapper = new ModelMapper();

    @Mock
    CakeRepository cakeRepository;

    CakeServiceImpl serviceImplUnderTest;

    @BeforeEach
    public void setup() throws Exception {
        MockitoAnnotations.openMocks(this);
        serviceImplUnderTest = new CakeServiceImpl(modelMapper, cakeRepository);
    }

    @Test
    void getAvailableCakes() {
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

        Iterable<Cake> iterable = Arrays.asList(cake1, cake2);

        when(cakeRepository.findAll()).thenReturn(iterable);

        List<CakeDto> cakeDtos = serviceImplUnderTest.getAvailableCakes();

        assertFalse(cakeDtos.isEmpty());
        assertEquals(2, cakeDtos.size());
        assertEquals(cake1.getTitle(), cakeDtos.get(0).getTitle());
        assertEquals(cake2.getTitle(), cakeDtos.get(1).getTitle());
    }

    @Test
    void addCakes() {
        // TODO
    }
}