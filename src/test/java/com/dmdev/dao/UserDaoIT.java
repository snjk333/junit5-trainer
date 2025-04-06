package com.dmdev.dao;

import com.dmdev.entity.Gender;
import com.dmdev.entity.Role;
import com.dmdev.entity.User;
import com.dmdev.integration.IntegrationTestBase;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class UserDaoIT extends IntegrationTestBase {

    private final UserDao userDao = UserDao.getInstance();


    @Test
    void findAll() {
        //we insert users in @BeforeEach method

        List<User> actualResult = userDao.findAll();

        assertThat(actualResult).hasSize(5);
        List<String> userIds = actualResult.stream().map(User::getName).toList();
        assertThat(userIds).contains("Ivan", "Petr","Sveta","Vlad","Kate");

    }

    @Test
    void findById() {
        //we insert users in @BeforeEach method

        Optional<User> actualResult = userDao.findById(1);

        assertThat(actualResult).isPresent();
        User user = actualResult.get();
        assertThat(user.getName()).isEqualTo("Ivan");
    }

    @Test
    void save() {
        User user = getUser("test@gmail.com");

        User actualResult = userDao.save(user);

        assertThat(actualResult).isEqualTo(user);
        assertNotNull(actualResult.getId());
        assertThat(userDao.findAll()).hasSize(6);

    }

    @Test
    void findByEmailAndPassword() {
        User user = getUser("test@gmail.com");
        userDao.save(user);

        Optional<User> actualRes =
                userDao.findByEmailAndPassword("test@gmail.com", "password");

        assertThat(actualRes).isPresent();
        assertThat(actualRes.get()).isEqualTo(user);

    }

    @Test
    void shouldNotFindByEmailAndPasswordIfUserDoesNotExist() {
        Optional<User> actualRes =
                userDao.findByEmailAndPassword(any(), any());
        assertThat(actualRes).isEmpty();

    }

    @Test
    void deleteSuccess() {
        User user = userDao.save(getUser("test@gmail.com"));

        var actualResult = userDao.delete(user.getId());
        assertThat(actualResult).isTrue();
    }

    @Test
    void deleteFailureIfUserDoesNotExist() {
        var actualResult = userDao.delete(0);
        assertThat(actualResult).isFalse();
    }

    @Test
    void update() {
        User user = getUser("test@gmail.com");
        userDao.save(user);
        user.setName("UpdatedName");

        userDao.update(user);

        var UpdatedUser = userDao.findById(user.getId()).get();
        assertThat(UpdatedUser).isEqualTo(user);
    }




    private static User getUser(String email) {
        return User.builder()
                .name("Jon")
                .email(email)
                .password("password")
                .birthday(LocalDate.of(2000, 3, 25))
                .role(Role.USER)
                .gender(Gender.MALE)
                .build();
    }
}