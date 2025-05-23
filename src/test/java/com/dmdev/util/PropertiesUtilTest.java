package com.dmdev.util;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class PropertiesUtilTest {

    static Stream<Arguments> getProperties() {
        return Stream.of(
                Arguments.of("db.url", "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"),
                Arguments.of("db.user", "sa"),
                Arguments.of("db.password", "")
        );
    }

    @ParameterizedTest
    @MethodSource("getProperties")
    void checkGet(String key, String value) {
        String actualResult = PropertiesUtil.get(key);
        Assertions.assertThat(actualResult).isEqualTo(value);
    }

}