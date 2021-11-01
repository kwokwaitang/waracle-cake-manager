package com.waracle.cake_manager.service;

import com.waracle.cake_manager.dto.CakeDto;
import com.waracle.cake_manager.model.Cake;
import com.waracle.cake_manager.repository.CakeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

class StartupRunnerServiceImplTest {

    @Mock
    CakeRepository cakeRepository;

    @Mock
    CakeDataService cakeDataService;

    StartupRunnerServiceImpl serviceImplUnderTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        serviceImplUnderTest = new StartupRunnerServiceImpl(cakeRepository, cakeDataService);
    }

    @Test
    @DisplayName("Testing for an unavailable cake repository")
    void constructorWithUnavailableCakeRepository() {
        Exception exception = Assertions.assertThrows(NullPointerException.class, () -> {
            new StartupRunnerServiceImpl(null, cakeDataService);
        });

        String expectedMessage = "Unavailable cake repository";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("An alternative (BDD) way of testing for an unavailable cake repository")
    void constructorWithUnavailableCakeRepositoryAlternative() {
        assertThatThrownBy(() -> new StartupRunnerServiceImpl(null, cakeDataService))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Unavailable cake repository");

        then(cakeRepository).shouldHaveNoInteractions();
        then(cakeDataService).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("Testing for an unavailable cake data service")
    void constructorWithUnavailableCakeDataService() {
        Exception exception = Assertions.assertThrows(NullPointerException.class, () -> {
            new StartupRunnerServiceImpl(cakeRepository, null);
        });

        String expectedMessage = "Unavailable cake data service";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void saveCakeData_withCakeDtos() {
        Cake cake = new Cake();
        cake.setTitle("Pound Cake");
        cake.setDescription("Made with a pound of each of the main ingredients (flour, butter, and sugar)");
        cake.setImageUrl("https://hips.hearstapps.com/hmg-prod.s3.amazonaws" +
                ".com/images/chocolate-matcha-swirl-pound-cake-1552946276.jpg?crop=1.00xw:0.857xh;0,0&resize=980:*");

        List<Cake> cakes = new ArrayList<>();
        cakes.add(cake);

        when(cakeRepository.saveAll(any(ArrayList.class))).thenReturn(cakes);

        CakeDto cakeDto = new CakeDto();
        cakeDto.setTitle("Pound Cake");
        cakeDto.setDescription("Made with a pound of each of the main ingredients (flour, butter, and sugar)");
        cakeDto.setImage("https://hips.hearstapps.com/hmg-prod.s3.amazonaws" +
                ".com/images/chocolate-matcha-swirl-pound-cake-1552946276.jpg?crop=1.00xw:0.857xh;0,0&resize=980:*");

        List<CakeDto> cakeDtos = new ArrayList<>();
        cakeDtos.add(cakeDto);

        List<Cake> actual = serviceImplUnderTest.saveCakeData(cakeDtos);
        assertEquals(1, actual.size());
        assertEquals(cake.getTitle(), actual.get(0).getTitle());
        assertEquals(cake.getDescription(), actual.get(0).getDescription());
        assertEquals(cake.getImageUrl(), actual.get(0).getImageUrl());

        verify(cakeRepository, times(1)).saveAll(anyList());
    }

    @Test
    void saveCakeData_withNoCakeDtos() {
        List<Cake> actual = serviceImplUnderTest.saveCakeData(new ArrayList<>());

        assertEquals(0, actual.size());
        assertEquals(Collections.emptyList(), actual);
    }
}