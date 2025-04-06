package com.dmdev.service;

import com.dmdev.dao.UserDao;
import com.dmdev.dto.UserDto;
import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.mapper.CreateUserMapper;
import com.dmdev.mapper.UserMapper;
import com.dmdev.validator.CreateUserValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private CreateUserValidator createUserValidator;
    @Mock
    private UserDao userDao;
    @Mock
    private CreateUserMapper createUserMapper;
    @Mock
    private UserMapper userMapper;

//    @InjectMocks
//    private UserService userService;
    //in UserService we don't have DI, so I can't inject mocks
    // lets try using Reflection API xD

    private final UserService userService = UserService.getInstance();

    @BeforeEach
    void setUp() throws Exception {
        try(var openMocks = MockitoAnnotations.openMocks(this)) {
            injectManualMocks(userService, "userDao", userDao);
            injectManualMocks(userService, "createUserValidator", createUserValidator);
            injectManualMocks(userService, "createUserMapper", createUserMapper);
            injectManualMocks(userService, "userMapper", userMapper);
        }
    }
    private void injectManualMocks(Object target, String fieldName, Object mock) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, mock);
    }

    @Test
    void loginSuccess(){
        User user = getUser();

        UserDto userDto = getUserDTO();


        Mockito.doReturn(Optional.of(user))
                .when(userDao)
                .findByEmailAndPassword(user.getEmail(), user.getPassword());
        Mockito.doReturn(userDto).when(userMapper).map(user);

        Optional<UserDto> actualResult = userService.login(user.getEmail(), user.getPassword());

        Assertions.assertThat(actualResult).isPresent();
        Assertions.assertThat(actualResult.get()).isEqualTo(userDto);

    }


    @Test
    void loginFailure(){
        Mockito.doReturn(Optional.empty())
                .when(userDao)
                .findByEmailAndPassword(any(), any());

        Optional<UserDto> actualResult = userService.login("123","123");

        Assertions.assertThat(actualResult).isEmpty();
        verifyNoInteractions(userMapper);
    }

    private static User getUser() {
        return User.builder()
                .id(10)
                .name("Jon")
                .email("jon@gmail.com")
                .password("password")
                .birthday(LocalDate.of(2000, 3, 25))
                .role(Role.USER)
                .gender(Gender.MALE)
                .build();
    }

    private static UserDto getUserDTO() {
        return UserDto.builder()
                .id(10)
                .name("Jon")
                .email("jon@gmail.com")
                .birthday(LocalDate.of(2000, 3, 25))
                .role(Role.USER)
                .gender(Gender.MALE)
                .build();
    }




}