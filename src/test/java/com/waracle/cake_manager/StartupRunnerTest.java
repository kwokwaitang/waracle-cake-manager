package com.waracle.cake_manager;

import com.waracle.cake_manager.service.StartupRunnerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.then;

class StartupRunnerTest {

    @Mock
    StartupRunnerService startupRunnerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void constructorWithMissingStartupRunnerService() {
        assertThatThrownBy(() -> new StartupRunner(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("Missing a start-up runner service");

        then(startupRunnerService).shouldHaveNoInteractions();
    }
}