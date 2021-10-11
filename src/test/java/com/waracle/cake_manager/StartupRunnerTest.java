package com.waracle.cake_manager;

import com.waracle.cake_manager.repository.CakeRepository;
import com.waracle.cake_manager.service.StartupRunnerService;
import org.apache.http.client.HttpClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class StartupRunnerTest {

    @Mock
    StartupRunnerService startupRunnerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void constructorWithMissingStartupRunnerService() {
        Exception exception = Assertions.assertThrows(NullPointerException.class, () -> {
            new StartupRunner(null);
        });

        String expectedMessage = "Missing a start-up runner service";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}