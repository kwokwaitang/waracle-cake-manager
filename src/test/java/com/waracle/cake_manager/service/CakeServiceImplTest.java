package com.waracle.cake_manager.service;

import com.waracle.cake_manager.dto.CakeDto;
import com.waracle.cake_manager.model.Cake;
import com.waracle.cake_manager.repository.CakeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

class CakeServiceImplTest {

    @Mock
    ModelMapper modelMapper;

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
        cake1.setTitle("The Biscoff Cake");
        cake1.setDescription("Vanilla sponge topped with Lotus biscuits");
        cake1.setImageUrl("https://cdn.shopify.com/s/files/1/0490/6418/1918/products/DD_Lotus_Cake_Full-scaled-1.jpg?v=1602446203");

        List<Cake> cakes = new ArrayList<>();
        cakes.add(cake1);

        when(cakeRepository.findAll()).thenReturn(cakes);

//        CakeDto cakeDto = new CakeDto();
//        cakeDto.setEmployeeId(1L);
//        cakeDto.setTitle("The Biscoff Cake");
//        cakeDto.setDescription("Vanilla sponge topped with Lotus biscuits");
//        cakeDto.setImage("https://cdn.shopify.com/s/files/1/0490/6418/1918/products/DD_Lotus_Cake_Full-scaled-1.jpg?v=1602446203");
//
//        List<CakeDto> cakeDtos = new ArrayList<>();
//        cakeDtos.add(cakeDto);
//
//        when(serviceImplUnderTest.getCakes(cakes)).thenReturn(cakeDtos);

        List<CakeDto> cakeDtos = serviceImplUnderTest.getAvailableCakes();

        assertEquals(1, cakeDtos.size());
        assertFalse(cakeDtos.isEmpty());

//        assertTrue(serviceImplUnderTest.getAvailableCakes().get(0) instanceof List);
//        assertEquals("The Biscoff Cake", serviceImplUnderTest.getAvailableCakes().get(0).getTitle());
    }

    @Test
    void addCakes() {
    }
}