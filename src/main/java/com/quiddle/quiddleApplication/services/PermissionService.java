package com.quiddle.quiddleApplication.services;

import com.quiddle.quiddleApplication.models.Permission;
import org.springframework.stereotype.Service;

@Service
public interface PermissionService {
     Permission create(Permission permission);
}
