package com.dmdev.mapper;

import com.dmdev.dto.CreateUserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CreateUserMapperTest {

    private final CreateUserMapper mapper = CreateUserMapper.getInstance();


    @Test
    void map(){
        CreateUserDto dto = CreateUserDto.builder()
                .name("Jon")
                .email("jon@gmail.com")
                .password("password")
                .birthday("2000-03-25")
                .role(Role.USER.name())
                .gender(Gender.MALE.name())
                .build();

        User actualRes = mapper.map(dto);

        User expectedRes = User.builder()
                .name("Jon")
                .email("jon@gmail.com")
                .password("password")
                .birthday(LocalDate.of(2000, 3, 25))
                .role(Role.USER)
                .gender(Gender.MALE)
                .build();
        Assertions.assertThat(actualRes).isEqualTo(expectedRes);
    }


}