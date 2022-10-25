package com.quiddle.quiddleApplication.services;

import com.quiddle.quiddleApplication.models.Role;
import com.quiddle.quiddleApplication.models.User;
import org.springframework.stereotype.Service;

@Service
public interface UserRoleService {
     User assignRoleToUser(Role role, User user);
}
