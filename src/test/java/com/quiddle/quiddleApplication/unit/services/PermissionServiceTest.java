package com.quiddle.quiddleApplication.unit.services;

import com.quiddle.quiddleApplication.models.Permission;
import com.quiddle.quiddleApplication.repositories.PermissionRepository;
import com.quiddle.quiddleApplication.services.FakeDataService;
import com.quiddle.quiddleApplication.services.PermissionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class PermissionServiceTest {
    @Autowired
    private PermissionService permissionService;

    @MockBean
    private PermissionRepository permissionRepository;

    @Autowired
    private FakeDataService fakeDataService;

    @Test
    void create_Successful() {
        //GIVEN
        Permission permission = fakeDataService.getPermission();
        Long expectedPermissionId = 1L;
        permission.setId(expectedPermissionId);

        //MOCK
        when(permissionRepository.save(any(Permission.class))).thenReturn(permission);

        permission = permissionService.create(permission);

        //TEST
        assertNotNull(permission);
        assertEquals(expectedPermissionId, permission.getId());
    }
}