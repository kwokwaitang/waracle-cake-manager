package com.waracle.cake_manager.service;

import com.waracle.cake_manager.dto.CakeDto;
import com.waracle.cake_manager.form.NewCakeDetails;
import com.waracle.cake_manager.model.NewCakeRequest;
import com.waracle.cake_manager.model.NewCakeResponse;

import java.util.List;

public interface CakeService {
    /**
     * Retrieve a list of available cakes from a database
     *
     * @return List of cakes
     */
    List<CakeDto> getAvailableCakes();

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
    NewCakeResponse addCake(NewCakeRequest newCakeRequest);

    /**
     * Add a new cake to the system via a REST API
     *
     * @param newCakeRequest Cake details in JSON
     * @return
     */
    NewCakeResponse addCakeViaRestApi(NewCakeRequest newCakeRequest);

    NewCakeRequest getNewCakeRequest(NewCakeDetails newCakeDetails);

    NewCakeRequest getNewCakeRequest(String title, String description, String image);
}
