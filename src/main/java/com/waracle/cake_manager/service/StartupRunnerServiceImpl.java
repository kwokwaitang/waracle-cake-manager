package com.waracle.cake_manager.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waracle.cake_manager.advice.LogMethodAccess;
import com.waracle.cake_manager.dto.CakeDto;
import com.waracle.cake_manager.model.Cake;
import com.waracle.cake_manager.repository.CakeRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:application.properties")
public class StartupRunnerServiceImpl implements StartupRunnerService {

    private static final Logger LOGGER = Logger.getGlobal();

    @Value("${cake.data.url}")
    private String cakeUrl;

    private final CakeRepository cakeRepository;

    private final HttpClient httpClient;

    public StartupRunnerServiceImpl(CakeRepository cakeRepository, HttpClient httpClient) {
        this.cakeRepository = Objects.requireNonNull(cakeRepository, () -> "Missing a cake repository");
        this.httpClient = Objects.requireNonNull(httpClient, () -> "Missing an HTTP client");
    }

    /**
     * Just to illustrate that this method is called straight after the constructor has been executed
     * <p>
     * This method cannot have any parameters, must be void and should not be static
     */
    @PostConstruct
    public void runAfterObjectCreated() {
        LOGGER.info("PostContruct method called in StartupRunnerServiceImpl");
    }

    @Override
    public List<CakeDto> fetchJsonCakeData() {
        try {
            String jsonCakeData = getJsonCakeData();
            if (StringUtils.isNotBlank(jsonCakeData)) {
                ObjectMapper objectMapper = new ObjectMapper();
                return objectMapper.readValue(jsonCakeData, new TypeReference<List<CakeDto>>() {});
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

    private String getJsonCakeData() throws IOException {
        String jsonCakeData = "{}";

        HttpGet request = new HttpGet(cakeUrl);
        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            jsonCakeData = EntityUtils.toString(entity);
        }

        return jsonCakeData;
    }
}
