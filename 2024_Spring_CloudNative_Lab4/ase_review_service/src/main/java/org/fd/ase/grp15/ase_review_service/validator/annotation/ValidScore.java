package org.fd.ase.grp15.ase_review_service.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.fd.ase.grp15.ase_review_service.validator.ScoreValidator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ScoreValidator.class})
public @interface ValidScore {
    String message() default "分数必须为-2，-1，1，2中的一个，分别表示REJECT，WEAK_REJECT，WEAK_ACCEPT，ACCEPT";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
