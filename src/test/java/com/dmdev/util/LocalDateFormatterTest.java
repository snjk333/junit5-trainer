package com.dmdev.util;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class LocalDateFormatterTest {

    @Test
    void format() {
        String date = "2005-07-28"; //"yyyy-MM-dd"

        LocalDate actualResult = LocalDateFormatter.format(date);
        assertThat(actualResult).isEqualTo(LocalDate.of(2005, 7, 28));

    }

    @Test
    void shouldThrowExceptionIfDataInvalid(){
        String date = "2005-07-28 15:38";
        assertThrows(DateTimeParseException.class, () -> LocalDateFormatter.format(date));
    }

    @ParameterizedTest
    @MethodSource("getValidationArguments")
    void isValid(String date, boolean expected) {
        boolean actualResult = LocalDateFormatter.isValid(date);

        assertThat(actualResult).isEqualTo(expected);
    }


    static Stream<Arguments> getValidationArguments(){
        return Stream.of(
                Arguments.of("2005-07-28", true),
                Arguments.of("2005-07-28 15:38", false),
                Arguments.of("01-01-2021", false),
                Arguments.of(null, false)
        );

    }



}