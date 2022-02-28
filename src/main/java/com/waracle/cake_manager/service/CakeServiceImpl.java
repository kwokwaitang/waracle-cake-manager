package com.waracle.cake_manager.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waracle.cake_manager.advice.LogMethodAccess;
import com.waracle.cake_manager.dto.CakeDto;
import com.waracle.cake_manager.dto.NewCakeRequestDto;
import com.waracle.cake_manager.dto.NewCakeResponseDto;
import com.waracle.cake_manager.form.NewCakeDetails;
import com.waracle.cake_manager.model.Cake;
import com.waracle.cake_manager.repository.CakeRepository;
import com.waracle.cake_manager.utils.GeneralUtils;
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
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class CakeServiceImpl implements CakeService {

    private static final Logger LOGGER = Logger.getGlobal();
    private static final String CAKES_URL = "http://localhost:8080/cakes";

    private final ModelMapper modelMapper;

    private final HttpClient httpClient;

    private final CakeRepository cakeRepository;

    public CakeServiceImpl(ModelMapper modelMapper, HttpClient httpClient, CakeRepository cakeRepository) {
        this.modelMapper = Objects.requireNonNull(modelMapper, () -> "Missing a model mapper");
        this.httpClient = Objects.requireNonNull(httpClient, () -> "Missing an HTTP Client");
        this.cakeRepository = Objects.requireNonNull(cakeRepository, () -> "Missing a cake repository");
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
    public SortedSet<CakeDto> getAvailableCakesSorted() {
        List<Cake> cakes = (List<Cake>) cakeRepository.findAll();
        if (!cakes.isEmpty()) {
            return mapSortedSet(cakes, CakeDto.class);
        }

        return Collections.emptySortedSet();
    }

    @Override
    public List<CakeDto> getAvailableCakes(int limit) {
        List<Cake> cakes = (List<Cake>) cakeRepository.findAll();
        if (!cakes.isEmpty()) {
            return GeneralUtils.mapListWithLimit(cakes, CakeDto.class, limit);
        }

        return Collections.emptyList();
    }

    @LogMethodAccess
    @Override
    public List<CakeDto> getAvailableCakesViaRestApi() {
        HttpGet request = new HttpGet(CAKES_URL);
        try {
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String jsonCakeData = EntityUtils.toString(entity);
                ObjectMapper objectMapper = new ObjectMapper();

                return objectMapper.readValue(jsonCakeData, new TypeReference<List<CakeDto>>() {});
            }
        } catch (IOException e) {
            LOGGER.warning(() -> String.format("Problem encountered whilst fetching cakes from REST API [%s]",
                    ExceptionUtils.getStackTrace(e)));
        }

        return Collections.emptyList();
    }

    @LogMethodAccess
    @Override
    public NewCakeResponseDto addCake(NewCakeRequestDto newCakeRequestDto) {
        Cake cake = cakeRepository.save(modelMapper.map(newCakeRequestDto, Cake.class));
        LOGGER.info(() -> String.format("\tSaved cake is [%s]", cake));

        return new NewCakeResponseDto(cake.getEmployeeId());
    }

    @LogMethodAccess
    @Override
    public NewCakeResponseDto addCakeViaRestApi(NewCakeRequestDto newCakeRequestDto) {
        HttpPost request = new HttpPost(CAKES_URL);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            StringEntity requestEntity = new StringEntity(objectMapper.writeValueAsString(newCakeRequestDto),
                    ContentType.APPLICATION_JSON);
            request.setEntity(requestEntity);
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                String jsonCakeData = EntityUtils.toString(entity);
                return objectMapper.readValue(jsonCakeData, NewCakeResponseDto.class);
            }
        } catch (IOException e) {
            LOGGER.warning(() -> String.format("Problem encountered whilst saving a cake via a REST API [%s]",
                    ExceptionUtils.getStackTrace(e)));
        }

        return new NewCakeResponseDto();
    }

    @Override
    public NewCakeRequestDto getNewCakeRequestDto(NewCakeDetails newCakeDetails) {
        return getNewCakeRequestDto(newCakeDetails.getTitle(), newCakeDetails.getDescription(), newCakeDetails.getImage());
    }

    @Override
    public NewCakeRequestDto getNewCakeRequestDto(String title, String description, String image) {
        return NewCakeRequestDto.of(title, description, image);
    }

    /**
     * Generic conversion of a list of source objects of type S to a list of type T
     * <p>
     * See https://www.baeldung.com/java-modelmapper-lists
     *
     * @param sourceObjects List of source objects
     * @param targetClass   The target class
     * @param <S>           Source objects of type S
     * @param <T>           Target class
     * @return List of objects of type T
     */
    private <S, T> List<T> mapList(List<S> sourceObjects, Class<T> targetClass) {
        return sourceObjects
                .stream()
                .map(sourceObject -> modelMapper.map(sourceObject, targetClass))
                .collect(Collectors.toList());
    }

    private <S, T> SortedSet<T> mapSortedSet(List<S> sourceObjects, Class<T> targetClass) {
        return sourceObjects
                .stream()
                .map(sourceObject -> modelMapper.map(sourceObject, targetClass))
                .collect(Collectors.toCollection(TreeSet::new));
    }
}
