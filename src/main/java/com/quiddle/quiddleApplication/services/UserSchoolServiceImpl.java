package com.quiddle.quiddleApplication.services;

import com.quiddle.quiddleApplication.models.School;
import com.quiddle.quiddleApplication.models.User;
import com.quiddle.quiddleApplication.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserSchoolServiceImpl implements UserSchoolService {

    private final UserRepository userRepository;

    public UserSchoolServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User addUserToSchool(User user, School school) {
        user.setSchool(school);

        return userRepository.save(user);
    }

    @Override
    public void removeUserFromSchool(User user, School school) {
        user.setSchool(null);

        userRepository.save(user);
    }

    @Override
    public boolean userBelongsToSchool(User user, School school) {
        School userSchool = user.getSchool();

        return userSchool.equals(school);
    }
}
