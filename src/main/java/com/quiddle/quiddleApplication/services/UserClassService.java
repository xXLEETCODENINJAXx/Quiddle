package com.quiddle.quiddleApplication.services;

import com.quiddle.quiddleApplication.models.UserClass;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserClassService {
     UserClass addUserToClass(UserClass userClass);
     void removeUserFromClass(UserClass userClass);
     Optional<UserClass> getUserClassByUserIdAndClassId(Long studentId, Long classId);
}
