package com.waracle.cake_manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class StartupRunnerTest {

//    @Mock
//    StartupRunnerService startupRunnerService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    void constructorWithMissingStartupRunnerService() {
        Exception exception = Assertions.assertThrows(NullPointerException.class, () -> new StartupRunner(null));

        String expectedMessage = "Missing a start-up runner service";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}