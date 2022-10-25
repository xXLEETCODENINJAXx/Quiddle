package com.quiddle.quiddleApplication.services;

import com.quiddle.quiddleApplication.enums.ERole;
import com.quiddle.quiddleApplication.exceptions.ApplicationException;
import com.quiddle.quiddleApplication.models.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {
    User createUser(User user);

    User updateUser(User user) throws ApplicationException;

    Optional<User> getUserById(Long userId) throws ApplicationException;

    Optional<User> getUserByUsername(String username);

    boolean checkUserExist(User userObj);

    boolean isType(ERole student, User user);
}
