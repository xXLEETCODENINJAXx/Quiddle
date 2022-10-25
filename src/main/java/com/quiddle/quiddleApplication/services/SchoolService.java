package com.quiddle.quiddleApplication.services;

import com.quiddle.quiddleApplication.exceptions.ApplicationException;
import com.quiddle.quiddleApplication.models.School;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface SchoolService {
     School create(School school);

    List<School> getAllSchools();

    Optional<School> getSchoolById(Long schoolId) throws ApplicationException;

    Optional<School> getSchoolByName(String schoolName);
}
