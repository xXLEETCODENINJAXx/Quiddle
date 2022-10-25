package com.quiddle.quiddleApplication.unit.services;

import com.quiddle.quiddleApplication.models.Role;
import com.quiddle.quiddleApplication.models.User;
import com.quiddle.quiddleApplication.repositories.UserRepository;
import com.quiddle.quiddleApplication.services.UserRoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserRoleServiceTest {

    private static final String TEST_ROLE_NAME_2 = "TEST ROLE 2";
    private static final String TEST_ROLE_NAME_1 = "TEST ROLE 1";
    private static final String TEST_USER = "TEST USER";

    @Autowired
    private UserRoleService userRoleService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void assignRoleToUser() {
        //GIVEN
        Role newRole = Role.builder().name(TEST_ROLE_NAME_2).build();

        User user = getDefaultUser();
        user.setRole(newRole);

        //MOCK
        when(userRepository.save(any(User.class))).thenReturn(user);

        User updatedUser = userRoleService.assignRoleToUser(newRole, getDefaultUser());

        assertNotNull(updatedUser);
        assertEquals(TEST_ROLE_NAME_2, updatedUser.getRole().getName());
    }

    private User getDefaultUser(){
        Role role = Role.builder().name(TEST_ROLE_NAME_1).build();

        return User.builder()
                .username(TEST_USER)
                .role(role).build();
    }
}