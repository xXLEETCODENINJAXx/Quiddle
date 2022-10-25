package com.quiddle.quiddleApplication.services;

import com.quiddle.quiddleApplication.enums.ERole;
import com.quiddle.quiddleApplication.exceptions.ApplicationException;
import com.quiddle.quiddleApplication.models.User;
import com.quiddle.quiddleApplication.repositories.UserRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user) throws ApplicationException {
        getUserById(user.getId()).orElseThrow(() -> new ApplicationException(String.format("User with id %d does not exists", user.getId())));

        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean checkUserExist(User user) {
        return userRepository.count(Example.of(user, ExampleMatcher.matchingAny())) > 0;
    }

    @Override
    public boolean isType(ERole roleEnum, User user) {
        return user.getRole().getName().equals(roleEnum.name());
    }
}
