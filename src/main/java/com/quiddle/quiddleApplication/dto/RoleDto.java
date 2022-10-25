package com.quiddle.quiddleApplication.dto;

import com.quiddle.quiddleApplication.enums.ERole;
import com.quiddle.quiddleApplication.models.Role;

import java.util.ArrayList;

public class RoleDto {
    public static Role getRole(ERole roleEnum)
    {
        return Role.builder()
                .name(roleEnum.name())
                .permissions(new ArrayList<>())
                .build();
    }
}
