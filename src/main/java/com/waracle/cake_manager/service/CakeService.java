package com.waracle.cake_manager.service;

import com.waracle.cake_manager.dto.CakeDto;
import com.waracle.cake_manager.dto.NewCakeRequestDto;
import com.waracle.cake_manager.dto.NewCakeResponseDto;
import com.waracle.cake_manager.form.NewCakeDetails;

import java.util.List;
import java.util.SortedSet;

public interface CakeService {
    /**
     * Retrieve a list of available cakes from a database
     *
     * @return List of cakes
     */
    List<CakeDto> getAvailableCakes();

    SortedSet<CakeDto> getAvailableCakesSorted();

    /**
     * Retrieve a limited list of available cakes from a database
     *
     * @param limit Number of cakes to retrieve
     * @return List of cakes
     */
    List<CakeDto> getAvailableCakes(int limit);

    /**
     * Retrieve a list of available cakes from a REST API
     *
     * @return List of cakes
     */
    List<CakeDto> getAvailableCakesViaRestApi();

    /**
     * Add a new cake to the system
     *
     * @param newCakeRequest Cake details in JSON
     * @return
     */
    NewCakeResponseDto addCake(NewCakeRequestDto newCakeRequest);

    /**
     * Add a new cake to the system via a REST API
     *
     * @param newCakeRequest Cake details in JSON
     * @return
     */
    NewCakeResponseDto addCakeViaRestApi(NewCakeRequestDto newCakeRequest);

    NewCakeRequestDto getNewCakeRequestDto(NewCakeDetails newCakeDetails);

    NewCakeRequestDto getNewCakeRequestDto(String title, String description, String image);
}
