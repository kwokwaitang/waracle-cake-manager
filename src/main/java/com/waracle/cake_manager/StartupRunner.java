package com.waracle.cake_manager;

import com.waracle.cake_manager.advice.LogMethodAccess;
import com.waracle.cake_manager.dto.CakeDto;
import com.waracle.cake_manager.service.StartupRunnerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Set up the JSON cake data in the H2 embedded database
 */
@Component
public class StartupRunner implements CommandLineRunner {

    private final StartupRunnerService startupRunnerService;

    public StartupRunner(StartupRunnerService startupRunnerService) {
        this.startupRunnerService = Objects.requireNonNull(startupRunnerService, () -> "Missing a start-up runner service");
    }

    @LogMethodAccess
    @Override
    public void run(String... args) throws Exception {
        List<CakeDto> cakeDtos = startupRunnerService.fetchJsonCakeData();
        startupRunnerService.saveCakeData(cakeDtos);
    }
}
