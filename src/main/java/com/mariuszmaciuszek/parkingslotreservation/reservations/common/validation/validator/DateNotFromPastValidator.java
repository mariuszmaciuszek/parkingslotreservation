package com.mariuszmaciuszek.parkingslotreservation.reservations.common.validation.validator;

import com.mariuszmaciuszek.parkingslotreservation.reservations.common.DateTimeUtils;
import com.mariuszmaciuszek.parkingslotreservation.reservations.common.validation.constraint.DateNotFromPast;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DateNotFromPastValidator implements ConstraintValidator<DateNotFromPast, LocalDateTime> {
    @Override
    public void initialize(final DateNotFromPast constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(final LocalDateTime value, final ConstraintValidatorContext context) {
        return Objects.nonNull(value) && DateTimeUtils.now().isBefore(value);
    }
}
