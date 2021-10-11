package com.waracle.cake_manager;

import com.waracle.cake_manager.advice.LogMethodAccess;
import com.waracle.cake_manager.dto.CakeDto;
import com.waracle.cake_manager.service.StartupRunnerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * To run code at application start-up (once the Spring context is up & running, it will call the run() method) and
 * can also be done as a @Bean, see {@link CakeManagerApplication#AnotherCommandLineRunnerSetupAsBean()}
 * <p>
 * Can also have multiple CommandLineRunners in one application, use @Order to determine the running order
 * <p>
 * Set up the JSON cake data in the H2 embedded database
 */
@Component
@Order(1)
public class StartupRunner implements CommandLineRunner {

    private final StartupRunnerService startupRunnerService;

    public StartupRunner(StartupRunnerService startupRunnerService) {
        this.startupRunnerService = Objects.requireNonNull(startupRunnerService, () -> "Missing a start-up runner " +
                "service");
    }

    @LogMethodAccess
    @Override
    public void run(String... args) throws Exception {
        List<CakeDto> cakeDtos = startupRunnerService.fetchJsonCakeData();
        startupRunnerService.saveCakeData(cakeDtos);
    }
}
