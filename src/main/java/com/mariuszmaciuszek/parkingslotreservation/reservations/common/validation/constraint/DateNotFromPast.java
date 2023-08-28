package com.mariuszmaciuszek.parkingslotreservation.reservations.common.validation.constraint;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.mariuszmaciuszek.parkingslotreservation.reservations.common.validation.validator.DateNotFromPastValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {DateNotFromPastValidator.class})
public @interface DateNotFromPast {
    String message() default "Invalid value. The date cannot be null or neither from the past";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
