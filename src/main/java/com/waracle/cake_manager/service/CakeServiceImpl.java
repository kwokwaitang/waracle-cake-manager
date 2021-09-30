package com.waracle.cake_manager.service;

import com.waracle.cake_manager.advice.LogMethodAccess;
import com.waracle.cake_manager.dto.CakeDto;
import com.waracle.cake_manager.form.NewCakeDetails;
import com.waracle.cake_manager.model.Cake;
import com.waracle.cake_manager.model.NewCakeRequest;
import com.waracle.cake_manager.model.NewCakeResponse;
import com.waracle.cake_manager.repository.CakeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class CakeServiceImpl implements CakeService {

    private static final Logger LOGGER = Logger.getGlobal();

    private final ModelMapper modelMapper;

    private final CakeRepository cakeRepository;

    public CakeServiceImpl(ModelMapper modelMapper, CakeRepository cakeRepository) {
        this.modelMapper = Objects.requireNonNull(modelMapper, () -> "Missing a model mapper");
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

    @LogMethodAccess
    @Override
    public NewCakeResponse addCake(NewCakeRequest newCakeRequest) {
        Cake cake = cakeRepository.save(modelMapper.map(newCakeRequest, Cake.class));
        LOGGER.info(() -> String.format("\tSaved cake is [%s]", cake));

        return new NewCakeResponse(cake.getEmployeeId());
    }

    @Override
    public NewCakeRequest getNewCakeRequest(NewCakeDetails newCakeDetails) {
        return getNewCakeRequest(newCakeDetails.getTitle(), newCakeDetails.getDescription(), newCakeDetails.getImage());
    }

    @Override
    public NewCakeRequest getNewCakeRequest(String title, String description, String image) {
        return new NewCakeRequest(title, description, image);
    }

    private List<CakeDto> getCakes(List<Cake> cakes) {
        if (!cakes.isEmpty()) {
            return mapList(cakes, CakeDto.class);
        }

        return Collections.emptyList();
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
        return listOfEntities
                .stream()
                .map(element -> modelMapper.map(element, targetDtoClass))
                .collect(Collectors.toList());
    }
}
