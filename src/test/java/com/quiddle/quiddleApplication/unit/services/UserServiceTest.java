package com.quiddle.quiddleApplication.unit.services;

import com.quiddle.quiddleApplication.enums.ERole;
import com.quiddle.quiddleApplication.exceptions.ApplicationException;
import com.quiddle.quiddleApplication.models.User;
import com.quiddle.quiddleApplication.repositories.UserRepository;
import com.quiddle.quiddleApplication.services.FakeDataService;
import com.quiddle.quiddleApplication.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceTest {
    private static final Long TEST_USER_ID = 1L;
    @Autowired
    private UserService userService;
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private FakeDataService fakeDataService;

    @Test
    void createUser_Successful() {
        //GIVEN
        User user = fakeDataService.getUser();
        String expectedUsername = user.getUsername();

        //MOCK
        when(userRepository.save(any(User.class))).thenReturn(user);

        user = userService.createUser(user);

        //TEST
        assertNotNull(user);
        assertEquals(expectedUsername, user.getUsername());
    }

    @Test
    void updateUser_Successful_WhenUserExist() throws ApplicationException {
        //GIVEN
        User user = fakeDataService.getUser();
        String expectedNewUsername = "New username";
        user.setUsername(expectedNewUsername);

        //MOCK
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        user = fakeDataService.getUser();
        user.setId(TEST_USER_ID);

        user = userService.updateUser(user);

        //TEST
        assertNotNull(user);
        assertEquals(expectedNewUsername, user.getUsername());
    }

    @Test
    void updateUser_WhenUserExist_Successful() throws ApplicationException {
        //GIVEN
        String newUserName = "New username";
        User user = fakeDataService.getUser();
        user.setUsername(newUserName);
        user.setId(TEST_USER_ID);

        //MOCK
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        user = fakeDataService.getUser();
        user.setId(TEST_USER_ID);

        user = userService.updateUser(user);

        //TEST
        assertNotNull(user);
        assertEquals(newUserName, user.getUsername());
    }

    @Test
    void updateUser_WhenUserDoesNotExist_ThrowsApplicationException() {
        //MOCK
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        //TEST
        assertThrows(ApplicationException.class, () -> userService.updateUser(fakeDataService.getUser()));
    }

    @Test
    void getUserById_Successful() throws ApplicationException {
        //GIVEN
        User user = fakeDataService.getUser();
        Long expectedUserId = TEST_USER_ID;
        user.setId(expectedUserId);

        //MOCK
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.of(user));

        Optional<User> userOptional = userService.getUserById(user.getId());

        //TEST
        assertTrue(userOptional.isPresent());
        assertEquals(expectedUserId, user.getId());
    }

    @Test
    void getUserById_WhenIdDoesNotExist_ReturnsOptionalOfNull() throws ApplicationException {
        //MOCK
        when(userRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        //GIVEN
        Long nonExistingId = 1000L;

        Optional<User> userOptional = userService.getUserById(nonExistingId);

        //TEST
        assertFalse(userOptional.isPresent());
    }

    @Test
    void getUserByUsername_Successful() {
        //GIVEN
        User user = fakeDataService.getUser();
        String expectedUsername = user.getUsername();

        //MOCK
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.of(user));

        Optional<User> userOptional = userService.getUserByUsername(user.getUsername());

        //TEST
        assertTrue(userOptional.isPresent());
        assertEquals(expectedUsername, user.getUsername());
    }

    @Test
    void getUserByUsername_WhenUsernameDoesNotExist_ReturnsOptionalOfNull() {
        //MOCK
        when(userRepository.findByUsername(any(String.class))).thenReturn(Optional.empty());

        //GIVEN
        String nonExistingUsername = "dummy";

        Optional<User> userOptional = userService.getUserByUsername(nonExistingUsername);

        //TEST
        assertFalse(userOptional.isPresent());
    }

    @Test
    void checkUserExist_WhenUserIsFound_ReturnsTrue() {
        //GIVEN
        Long expectedExistingUserCount = 1L;
        //MOCK
        when(userRepository.count(any(Example.class))).thenReturn(expectedExistingUserCount);

        boolean userExist = userService.checkUserExist(fakeDataService.getUser());
        //TEST
        assertTrue(userExist);
    }

    @Test
    void checkUserExist_WhenUserIsNotFound_ReturnsFalse() {
        //GIVEN
        Long expectedExistingUserCount = 0L;
        //MOCK
        when(userRepository.count(any(Example.class))).thenReturn(expectedExistingUserCount);

        boolean userExist = userService.checkUserExist(fakeDataService.getUser());
        // TEST
        assertFalse(userExist);
    }

    @Test
    void isType() {
        //GIVEN
        User user = fakeDataService.getUser();
        user.setRole(fakeDataService.getRole());

        ERole expectedUserRole = ERole.USER;

        boolean isUser = userService.isType(expectedUserRole, user);

        //TEST
        assertTrue(isUser);
    }
}