package com.waracle.cake_manager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waracle.cake_manager.advice.LogMethodAccess;
import com.waracle.cake_manager.dto.CakeDto;
import com.waracle.cake_manager.form.NewCakeDetails;
import com.waracle.cake_manager.model.Cake;
import com.waracle.cake_manager.pojo.NewCakeRequest;
import com.waracle.cake_manager.pojo.NewCakeResponse;
import com.waracle.cake_manager.repository.CakeRepository;
import com.waracle.cake_manager.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class CakeServiceImpl implements CakeService {

    private static final Logger LOGGER = Logger.getGlobal();
    private static final String CAKES_URL = "http://localhost:8080/cakes";

    private final ModelMapper modelMapper;

    private final HttpClient httpClient;

    private final CakeRepository cakeRepository;

    private final UserRepository userRepository;

    public CakeServiceImpl(ModelMapper modelMapper, HttpClient httpClient, CakeRepository cakeRepository,
                           UserRepository userRepository) {
        this.modelMapper = Objects.requireNonNull(modelMapper, () -> "Missing a model mapper");
        this.httpClient = Objects.requireNonNull(httpClient, () -> "Missing an HTTP Client");
        this.cakeRepository = Objects.requireNonNull(cakeRepository, () -> "Missing a cake repository");
        this.userRepository = Objects.requireNonNull(userRepository, () -> "Missing a user repository");
    }

    @Override
    public List<CakeDto> getAvailableCakes() {
        List<Cake> cakes = (List<Cake>) cakeRepository.findAll();
        if (!cakes.isEmpty()) {
            return mapList(cakes, CakeDto.class);
        }

        return Collections.emptyList();
    }

    @Override
    public List<CakeDto> getCarrotCakes() {
        List<Cake> cakes = (List<Cake>) cakeRepository.findAll();

        List<Cake> carrotCakes = cakes.stream()
                .filter(cake -> cake.getTitle().toLowerCase().startsWith("carrot"))
                .collect(Collectors.toList());
        if (!carrotCakes.isEmpty()) {
            return mapList(carrotCakes, CakeDto.class);
        }

        return Collections.emptyList();
    }

    @LogMethodAccess
    @Override
    public List<CakeDto> getAvailableCakesViaRestApi() {
        List<CakeDto> availableCakes = Collections.emptyList();

        String jsonCakeData = fetchJsonCakeData();
        if (StringUtils.isNotBlank(jsonCakeData)) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                availableCakes = objectMapper.readValue(fetchJsonCakeData(), new TypeReference<List<CakeDto>>() {});
            } catch (JsonProcessingException e) {
                LOGGER.warning(() -> String.format("Problem encountered whilst processing the fetched JSON cake data " +
                        "[%s]", ExceptionUtils.getStackTrace(e)));
            }
        }

        return availableCakes;
    }

    private String fetchJsonCakeData() {
        HttpGet request = new HttpGet(CAKES_URL);
        request.addHeader("Authorization", "Bearer " + userRepository.findByUsername("user").getToken());

        try {
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return EntityUtils.toString(entity);
            }
        } catch (IOException e) {
            LOGGER.warning(() -> String.format("Problem encountered whilst fetching cakes from REST API [%s]",
                    ExceptionUtils.getStackTrace(e)));
        }

        return "";
    }

    @LogMethodAccess
    @Override
    public NewCakeResponse addCake(NewCakeRequest newCakeRequest) {
        Cake cake = cakeRepository.save(modelMapper.map(newCakeRequest, Cake.class));
        LOGGER.info(() -> String.format("\tSaved cake is [%s]", cake));

        return new NewCakeResponse(cake.getEmployeeId());
    }

    @LogMethodAccess
    @Override
    public NewCakeResponse addCakeViaRestApi(NewCakeRequest newCakeRequest) {
        HttpPost request = new HttpPost(CAKES_URL);
        request.addHeader("Authorization", "Bearer " + userRepository.findByUsername("user").getToken());

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            StringEntity requestEntity = new StringEntity(objectMapper.writeValueAsString(newCakeRequest),
                    ContentType.APPLICATION_JSON);
            request.setEntity(requestEntity);
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String jsonCakeData = EntityUtils.toString(entity);
                return objectMapper.readValue(jsonCakeData, NewCakeResponse.class);
            }
        } catch (IOException e) {
            LOGGER.warning(() -> String.format("Problem encountered whilst saving a cake via a REST API [%s]",
                    ExceptionUtils.getStackTrace(e)));
        }

        return new NewCakeResponse();
    }

    @Override
    public NewCakeRequest getNewCakeRequest(NewCakeDetails newCakeDetails) {
        return getNewCakeRequest(newCakeDetails.getTitle(), newCakeDetails.getDescription(), newCakeDetails.getImage());
    }

    @Override
    public NewCakeRequest getNewCakeRequest(String title, String description, String image) {
        return new NewCakeRequest(title, description, image);
    }

    /**
     * Generic conversion of a List of entities to a list of DTOs
     * <p>
     * See https://www.baeldung.com/java-modelmapper-lists
     *
     * @param listOfEntities
     * @param targetDtoClass
     * @param <S>            The entity object
     * @param <T>            The target DTO class
     * @return The equivalent list of DTO objects
     */
    private <S, T> List<T> mapList(List<S> listOfEntities, Class<T> targetDtoClass) {
        return listOfEntities.stream()
                .map(element -> modelMapper.map(element, targetDtoClass))
                .collect(Collectors.toList());
    }
}
