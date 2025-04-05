package com.dmdev.validator;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CreateUserValidatorTest {
    private final CreateUserValidator validator = CreateUserValidator.getInstance();

    @Test
    void shouldPassValidation() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Jon")
                .email("jon@gmail.com")
                .password("password")
                .birthday("2000-03-25")
                .role(Role.USER.name())
                .gender(Gender.MALE.name())
                .build();
        ValidationResult actualRes = validator.validate(dto);
        assertTrue(actualRes.isValid());
    }

    @Test
    void invalidBirthday() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Jon")
                .email("jon@gmail.com")
                .password("password")
                .birthday("30-12-2020")
                .role(Role.USER.name())
                .gender(Gender.MALE.name())
                .build();
        ValidationResult actualRes = validator.validate(dto);

        assertThat(actualRes.getErrors()).hasSize(1);
        assertThat(actualRes.getErrors().get(0).getCode()).isEqualTo("invalid.birthday");
    }

    @Test
    void invalidRole() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Jon")
                .email("jon@gmail.com")
                .password("password")
                .birthday("2000-03-25")
                .role("NOROLELALALA")
                .gender(Gender.MALE.name())
                .build();
        ValidationResult actualRes = validator.validate(dto);

        assertThat(actualRes.getErrors()).hasSize(1);
        assertThat(actualRes.getErrors().get(0).getCode()).isEqualTo("invalid.role");
    }

    @Test
    void invalidGender() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Jon")
                .email("jon@gmail.com")
                .password("password")
                .birthday("2000-03-25")
                .role(Role.USER.name())
                .gender("NotNotNotGender")
                .build();
        ValidationResult actualRes = validator.validate(dto);

        assertThat(actualRes.getErrors()).hasSize(1);
        assertThat(actualRes.getErrors().get(0).getCode()).isEqualTo("invalid.gender");
    }

    @Test
    void InvalidBirthdayAndRole() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Jon")
                .email("jon@gmail.com")
                .password("password")
                .birthday("30-12-2020")
                .role("NOROLELALALA")
                .gender(Gender.MALE.name())
                .build();
        ValidationResult actualRes = validator.validate(dto);
        assertThat(actualRes.getErrors()).hasSize(2);
        assertThat(actualRes.getErrors().get(0).getCode()).isEqualTo("invalid.birthday");
        assertThat(actualRes.getErrors().get(1).getCode()).isEqualTo("invalid.role");
    }

    @Test
    void InvalidBirthdayAndRoleAndGender() {
        CreateUserDto dto = CreateUserDto.builder()
                .name("Jon")
                .email("jon@gmail.com")
                .password("password")
                .birthday("30-12-2020")
                .role("NOROLELALALA")
                .gender("NotNotNotGender")
                .build();
        ValidationResult actualRes = validator.validate(dto);
        assertThat(actualRes.getErrors()).hasSize(3);

        List<String> errorCodes = actualRes.getErrors().stream().map(Error::getCode).toList();

        assertThat(errorCodes).contains("invalid.birthday", "invalid.role", "invalid.gender");
    }
}