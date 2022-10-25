package com.quiddle.quiddleApplication.services;

import com.quiddle.quiddleApplication.models.Permission;
import com.quiddle.quiddleApplication.repositories.PermissionRepository;
import org.springframework.stereotype.Service;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Permission create(Permission permission) {
        return permissionRepository.save(permission);
    }
}
