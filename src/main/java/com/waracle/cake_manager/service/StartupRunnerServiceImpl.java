package com.waracle.cake_manager.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waracle.cake_manager.dto.CakeDto;
import com.waracle.cake_manager.model.Cake;
import com.waracle.cake_manager.repository.CakeRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class StartupRunnerServiceImpl implements StartupRunnerService {

    private static final Logger LOGGER = Logger.getGlobal();

    private final CakeRepository cakeRepository;

    private final CakeDataService cakeDataService;

    public StartupRunnerServiceImpl(CakeRepository cakeRepository, CakeDataService cakeDataService) {
        this.cakeRepository = Objects.requireNonNull(cakeRepository, () -> "Unavailable cake repository");
        this.cakeDataService = Objects.requireNonNull(cakeDataService, () -> "Unavailable cake data service");
    }

    /**
     * Just to illustrate that this method is called straight after the constructor has been executed
     * <p>
     * This method cannot have any parameters, must be void and should not be static
     */
    @PostConstruct
    public void runAfterObjectCreated() {
        LOGGER.info("PostConstruct method called in StartupRunnerServiceImpl");
    }

    @Override
    public List<CakeDto> fetchJsonCakeData() {
        try {
            String jsonCakeData = cakeDataService.get();
            if (StringUtils.isNotBlank(jsonCakeData)) {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(jsonCakeData, new TypeReference<List<CakeDto>>() {
                });
            }
        } catch (IOException e) {
            LOGGER.warning(() -> String.format("Problem encountered when fetching cake data [%s]",
                    ExceptionUtils.getStackTrace(e)));
        }

        return Collections.emptyList();
    }

    @Override
    public List<Cake> saveCakeData(List<CakeDto> cakeDtos) {
        if (cakeDtos != null && !cakeDtos.isEmpty()) {
            LOGGER.info(() -> String.format("\tCakes [%s]", cakeDtos));

            List<Cake> cakesForDb = cakeDtos.stream()
                    .map((CakeDto cakeDto) -> new Cake(
                            cakeDto.getTitle(),
                            cakeDto.getDescription(),
                            cakeDto.getImage())
                    ).collect(Collectors.toList());

            return (List<Cake>) cakeRepository.saveAll(cakesForDb);
        }

        return Collections.emptyList();
    }
}
