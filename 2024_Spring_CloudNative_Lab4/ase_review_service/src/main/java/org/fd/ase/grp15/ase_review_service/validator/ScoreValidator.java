package org.fd.ase.grp15.ase_review_service.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.fd.ase.grp15.ase_review_service.validator.annotation.ValidScore;

public class ScoreValidator implements ConstraintValidator<ValidScore, Integer> {

    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return integer == -2 || integer == -1 || integer == 1 || integer == 2;
    }
}
