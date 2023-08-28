package com.mariuszmaciuszek.parkingslotreservation.reservations.common.validation.validator;

import static org.assertj.core.api.Assertions.assertThat;

import com.mariuszmaciuszek.parkingslotreservation.reservations.common.DateTimeUtils;
import org.junit.jupiter.api.Test;

class DateNotFromPastValidatorTest {
    private final DateNotFromPastValidator validator = new DateNotFromPastValidator();

    @Test
    void isValid_Should_ReturnTrue() {
        final boolean result = validator.isValid(DateTimeUtils.now().plusHours(2), null);
        assertThat(result).isTrue();
    }

    @Test
    void isValid_Should_ReturnFalse() {
        final boolean result = validator.isValid(DateTimeUtils.now().minusDays(2), null);
        assertThat(result).isFalse();
    }

    @Test
    void isValid_Should_ReturnFalse_When_DateIsNull() {
        final boolean result = validator.isValid(null, null);
        assertThat(result).isFalse();
    }
}
