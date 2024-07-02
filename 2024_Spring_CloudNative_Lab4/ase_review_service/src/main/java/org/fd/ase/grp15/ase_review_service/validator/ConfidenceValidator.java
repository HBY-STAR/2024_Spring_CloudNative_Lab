package org.fd.ase.grp15.ase_review_service.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.fd.ase.grp15.ase_review_service.validator.annotation.ValidConfidence;

public class ConfidenceValidator implements ConstraintValidator<ValidConfidence, Integer> {
    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return integer == -2 || integer == -1 || integer == 1 || integer == 2;
    }
}
