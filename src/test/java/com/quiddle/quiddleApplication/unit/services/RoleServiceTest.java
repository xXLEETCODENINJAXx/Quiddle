package com.quiddle.quiddleApplication.unit.services;

import com.quiddle.quiddleApplication.enums.ERole;
import com.quiddle.quiddleApplication.exceptions.ApplicationException;
import com.quiddle.quiddleApplication.models.Role;
import com.quiddle.quiddleApplication.repositories.RoleRepository;
import com.quiddle.quiddleApplication.services.FakeDataService;
import com.quiddle.quiddleApplication.services.RoleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class RoleServiceTest  {
    @Autowired
    private RoleService roleService;

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private FakeDataService fakeDataService;

    @Test
    void create_Successful() {
        //GIVEN
        Role role = fakeDataService.getRole();
        Long expectedRoleId = 1L;
        role.setId(expectedRoleId);

        //MOCK
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        role = roleService.create(role);

        //TEST
        assertNotNull(role);
        assertEquals(expectedRoleId, role.getId());
    }


    @Test
    void getRoleById_Successful() throws ApplicationException {
        //GIVEN
        Role role = fakeDataService.getRole();
        Long expectedRoleId = 1L;
        role.setId(expectedRoleId);

        //MOCK
        when(roleRepository.findById(any(Long.class))).thenReturn(Optional.of(role));

        Optional<Role> roleOptional = roleService.getRoleById(role.getId());

        //TEST
        assertTrue(roleOptional.isPresent());
        assertEquals(expectedRoleId, role.getId());
    }

    @Test
    void getRoleById_WhenIdDoesNotExist_ReturnsOptionalOfNull() throws ApplicationException {
        //MOCK
        when(roleRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        //GIVEN
        Long nonExistingId = 1000L;

        Optional<Role> roleOptional = roleService.getRoleById(nonExistingId);

        //TEST
        assertFalse(roleOptional.isPresent());
    }


    @Test
    void getRoleByName_WhenRoleExist_Successful() throws ApplicationException {
        //GIVEN
        Role role = fakeDataService.getRole();
        Long expectedRoleId = 1L;
        role.setId(expectedRoleId);

        //MOCK
        when(roleRepository.findByName(any(String.class))).thenReturn(Optional.of(role));

        Optional<Role> roleOptional = roleService.getRole(ERole.ADMIN);

        //TEST
        assertTrue(roleOptional.isPresent());
        assertEquals(expectedRoleId, roleOptional.get().getId());
    }

    @Test
    void getRoleByName_WhenRoleDoesNotExist_ReturnsOptionalOfNull() throws ApplicationException {
        //MOCK
        when(roleRepository.findByName(any(String.class))).thenReturn(Optional.empty());

        //GIVEN
        String nonExistingName = "dummy";

        Optional<Role> userOptional = roleService.getRole(ERole.USER);

        //TEST
        assertFalse(userOptional.isPresent());
    }

    @Test
    void getAllRoles() {
        //GIVEN
        List<Role> roles = List.of(fakeDataService.getRole());

        //MOCK
        when(roleRepository.findAll()).thenReturn(roles);

        roles = roleService.getAllRoles();

        int expectedNumberOfRoles = 1;
        //TEST
        assertEquals(expectedNumberOfRoles, roles.size());
    }
}