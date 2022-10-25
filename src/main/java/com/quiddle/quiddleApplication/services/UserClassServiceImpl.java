package com.quiddle.quiddleApplication.services;

import com.quiddle.quiddleApplication.dto.UserClassDto;
import com.quiddle.quiddleApplication.models.UserClass;
import com.quiddle.quiddleApplication.repositories.UserClassRepository;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserClassServiceImpl implements UserClassService {

    private final UserClassRepository userClassRepository;

    public UserClassServiceImpl(UserClassRepository userClassRepository) {
        this.userClassRepository = userClassRepository;
    }

    @Override
    public UserClass addUserToClass(UserClass userClass) {
        return userClassRepository.save(userClass);
    }

    @Override
    public void removeUserFromClass(UserClass userClass) {
        userClassRepository.delete(userClass);
    }

    @Override
    public Optional<UserClass> getUserClassByUserIdAndClassId(Long studentId, Long classId) {
        Example<UserClass> example = Example.of(UserClassDto.getUserClass(studentId, classId));

        return userClassRepository.findOne(example);
    }
}
