package com.quiddle.quiddleApplication.services;

import com.quiddle.quiddleApplication.models.School;
import com.quiddle.quiddleApplication.models.User;
import org.springframework.stereotype.Service;

@Service
public interface UserSchoolService {

    User addUserToSchool(User user, School school);

    void removeUserFromSchool(User user, School school);

    boolean userBelongsToSchool(User teacher, School school);
}
