package com.waracle.cake_manager.controller;

import com.waracle.cake_manager.advice.LogMethodAccess;
import com.waracle.cake_manager.dto.CakeDto;
import com.waracle.cake_manager.dto.NewCakeRequestDto;
import com.waracle.cake_manager.dto.NewCakeResponseDto;
import com.waracle.cake_manager.service.CakeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.SortedSet;
import java.util.logging.Logger;

/**
 * {@link org.springframework.web.bind.annotation.RestController} represents both {@link
 * org.springframework.stereotype.Controller} and {@link
 * org.springframework.web.bind.annotation.ResponseBody} (which tells controller that the object
 * returned is automatically serialized into JSON and passed back into the HttpResponse object)
 */
@RestController
public class CakeRestApiController {

    private static final Logger LOGGER = Logger.getGlobal();

    private final CakeService cakeService;

    public CakeRestApiController(CakeService cakeService) {
        this.cakeService = Objects.requireNonNull(cakeService, () -> "Missing a cake service");
    }

    /**
     * "By accessing an alternative endpoint (/cakes) with an appropriate client it must be possible to download a
     * list of the cakes currently in the system as JSON data."
     *
     * @return
     */
    @LogMethodAccess
    @GetMapping("/cakes")
    public ResponseEntity<List<CakeDto>> getListOfCakes() {
        return ResponseEntity.ok(cakeService.getAvailableCakes());
    }

    @LogMethodAccess
    @GetMapping("/cakes-in-sorted-order")
    public ResponseEntity<SortedSet<CakeDto>> getSortedCakes() {
        return ResponseEntity.ok(cakeService.getAvailableCakesSorted());
    }

    /**
     * "The /cakes endpoint must also allow new cakes to be created."
     * <p>
     * An example JSON to post...
     * <pre>
     *     {
     *          "title" : "The Biscoff Cake",
     *          "description" : "Vanilla sponge topped with Lotus biscuits",
     *          "imageUrl" : "https://cdn.shopify.com/s/files/1/0490/6418/1918/products/DD_Lotus_Cake_Full-scaled-1.jpg?v=1602446203"
     *      }
     * </pre>
     *
     * @param newCakeRequestDto
     * @return Response containing the primary value of the newly added cake
     */
    @LogMethodAccess
    @PostMapping("/cakes")
    public ResponseEntity<NewCakeResponseDto> newCakeDetails(@RequestBody NewCakeRequestDto newCakeRequestDto) {
        return ResponseEntity.ok(cakeService.addCake(newCakeRequestDto));
    }
}
