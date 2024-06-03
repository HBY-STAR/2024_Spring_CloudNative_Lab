package org.fd.ase.grp15.ase_review_service.validator.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.fd.ase.grp15.ase_review_service.validator.ConfidenceValidator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ConfidenceValidator.class})
public @interface ValidConfidence {
    String message() default "置信度必须为-2，-1，1，2中的一个，分别表示VERY_LOW，LOW，HIGH，VERY_HIGH";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
