package com.waracle.cake_manager;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waracle.cake_manager.advice.LogMethodAccess;
import com.waracle.cake_manager.dto.CakeDto;
import com.waracle.cake_manager.model.Cake;
import com.waracle.cake_manager.repository.CakeRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Set up the JSON cake data in the H2 embedded database
 */
@Component
public class StartupRunner implements CommandLineRunner {

    private static final Logger LOGGER = Logger.getGlobal();
    private static final String CAKE_URL = "https://gist.githubusercontent" +
            ".com/hart88/198f29ec5114a3ec3460/raw/8dd19a88f9b8d24c23d9960f3300d0c917a4f07c/cake.json";

    private final CakeRepository cakeRepository;

    private final HttpClient httpClient;

    public StartupRunner(CakeRepository cakeRepository, HttpClient httpClient) {
        this.cakeRepository = Objects.requireNonNull(cakeRepository, () -> "Missing a cake service");
        this.httpClient = Objects.requireNonNull(httpClient, () -> "Missing an HTTP client");
    }

    @LogMethodAccess
    @Override
    public void run(String... args) throws Exception {
        String jsonCakeData = getJsonCakeData();
        if (StringUtils.isNotBlank(jsonCakeData)) {
            ObjectMapper objectMapper = new ObjectMapper();
            // When dealing with a collection in the JSON, use TypeReference
            List<CakeDto> cakes = objectMapper.readValue(jsonCakeData, new TypeReference<List<CakeDto>>() {});
            // Make sure there are cakes to save...
            if (cakes != null && !cakes.isEmpty()) {
                LOGGER.info(() -> String.format("\tCakes [%s]", cakes));

                List<Cake> cakesForDb = cakes.stream()
                        .map((CakeDto cakeDto) -> new Cake(
                                cakeDto.getTitle(),
                                cakeDto.getDescription(),
                                cakeDto.getImage())
                        ).collect(Collectors.toList());

                cakeRepository.saveAll(cakesForDb);
            }
        }
    }

    private String getJsonCakeData() throws IOException {
        String jsonCakeData = "{}";

        HttpGet request = new HttpGet(CAKE_URL);
        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            jsonCakeData = EntityUtils.toString(entity);
        }

        String finalJsonCakeData = jsonCakeData;
        LOGGER.info(() -> String.format("\tJSON cake data is %s", finalJsonCakeData));

        return jsonCakeData;
    }
}
