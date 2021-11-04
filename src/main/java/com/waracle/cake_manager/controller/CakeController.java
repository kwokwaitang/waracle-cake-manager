package com.waracle.cake_manager.controller;

import com.waracle.cake_manager.advice.LogMethodAccess;
import com.waracle.cake_manager.dto.NewCakeRequestDto;
import com.waracle.cake_manager.dto.NewCakeResponseDto;
import com.waracle.cake_manager.form.NewCakeDetails;
import com.waracle.cake_manager.form.validator.NewCakeDetailsFormValidator;
import com.waracle.cake_manager.service.CakeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.logging.Logger;

@Controller
public class CakeController {

    private static final Logger LOGGER = Logger.getGlobal();

    private final CakeService cakeService;

    /**
     * Note...
     * Tried to set up NewCakeDetailsFormValidator as a constructor injected dependency but that just messes up the unit
     * tests - no solution found as yet!?!?!?!
     *
     * @param cakeService
     */
    public CakeController(CakeService cakeService) {
        this.cakeService = Objects.requireNonNull(cakeService, () -> "Missing a cake service");
    }

    @InitBinder(value = "newCakeDetails")
    public void initBinderForNewCakeDetailsValidator(WebDataBinder binder) {
        binder.setValidator(new NewCakeDetailsFormValidator());
    }

    /**
     * "By accessing the root of the server (/) it should be possible to list the cakes currently in the system. This
     * must be presented in an acceptable format for a human to read."
     *
     * @param model
     * @return A view with a list of cakes in an acceptable format for a human to read
     */
    @LogMethodAccess
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("cakes", cakeService.getAvailableCakes(3));

        // To trigger a modal as soon as the index view is rendered
        //model.addAttribute("exceptionIssue", "Blah blah blah...");

        return "index";
    }

    /**
     * "It must be possible for a human to add a new cake to the server."
     * <p>
     * Display a form to capture new cake details
     *
     * @param model
     * @return The data capture form
     */
    @LogMethodAccess
    @GetMapping("/new-cake-details")
    public String captureNewCakeDetails(Model model) {
        model.addAttribute("newCakeDetails", new NewCakeDetails());
        model.addAttribute("formFragment", "new-cake-details");

        return "form";
    }

    /**
     * To handle the submission of the form for capturing new cake details
     *
     * @param newCakeDetails Captured form data
     * @param errors         From validation checks
     * @param model
     * @return The view, either the form (when there are issues with the form) or an indication if the cake details
     * have been saved to the database
     */
    @LogMethodAccess
    @PostMapping("/new-cake-details")
    public String onSubmit(@ModelAttribute("newCakeDetails") @Valid NewCakeDetails newCakeDetails,
                           final Errors errors, final Model model) {
        String view = "form";

        model.addAttribute("formFragment", "new-cake-details");

        Supplier<String> onSubmitMsg = () -> String.format("Problem with submitted details [%s]", newCakeDetails);
        if (!errors.hasErrors()) {
            onSubmitMsg = () -> String.format("\tNew cake details are fine [%s]", newCakeDetails);

            NewCakeRequestDto newCakeRequestDto = cakeService.getNewCakeRequestDto(newCakeDetails);
            NewCakeResponseDto newCakeResponseDto = cakeService.addCake(newCakeRequestDto);
            LOGGER.info(() -> String.format("\tResponse is [%s]", newCakeResponseDto));

            if (newCakeResponseDto.getId() != null) {
                view = "successfully-added-cake";
            } else {
                view = "unsuccessfully-added-cake";
            }
        }

        LOGGER.info(onSubmitMsg);

        return view;
    }
}
