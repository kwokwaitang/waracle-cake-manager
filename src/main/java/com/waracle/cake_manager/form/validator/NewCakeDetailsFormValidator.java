package com.waracle.cake_manager.form.validator;

import com.waracle.cake_manager.form.NewCakeDetails;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class NewCakeDetailsFormValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return NewCakeDetails.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof NewCakeDetails) {
            NewCakeDetails newSiteDetails = (NewCakeDetails) target;
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "error.title.missing");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "description", "error.description.missing");
            ValidationUtils.rejectIfEmptyOrWhitespace(errors, "image", "error.image.missing");

            // Perform additional checks on the captured form data but only if there is something present...
            if (!errors.hasFieldErrors("image")) {
                if (!imageStartsWithHttp(newSiteDetails.getImage())) {
                    errors.rejectValue("image", "error.image.wrong-format");
                }
            }
        }
    }

    private boolean imageStartsWithHttp(final String image) {
        return StringUtils.isNotBlank(image) && StringUtils.startsWithAny(image, "http:", "https:");
    }
}
