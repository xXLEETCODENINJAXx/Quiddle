package com.quiddle.quiddleApplication.services;

import com.quiddle.quiddleApplication.models.Role;
import com.quiddle.quiddleApplication.models.User;
import com.quiddle.quiddleApplication.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRepository userRepository;

    public UserRoleServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User assignRoleToUser(Role role, User user) {
        user.setRole(role);

        return userRepository.save(user);
    }
}
