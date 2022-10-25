package com.quiddle.quiddleApplication.dto;

import com.quiddle.quiddleApplication.models.Permission;

public class PermissionDto {
    public static Permission getPermission(String name) {
        return Permission.builder()
                .name(name)
                .build();
    }
}
