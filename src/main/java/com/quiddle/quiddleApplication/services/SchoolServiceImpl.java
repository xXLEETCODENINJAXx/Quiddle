package com.quiddle.quiddleApplication.services;

import com.quiddle.quiddleApplication.models.School;
import com.quiddle.quiddleApplication.repositories.SchoolRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;

    public SchoolServiceImpl(SchoolRepository schoolRepository) {
        this.schoolRepository = schoolRepository;
    }

    @Override
    public School create(School school) {
        return schoolRepository.save(school);
    }

    @Override
    public List<School> getAllSchools() {
        return schoolRepository.findAll();
    }

    @Override
    public Optional<School> getSchoolById(Long schoolId) {
        return schoolRepository.findById(schoolId);
    }

    @Override
    public Optional<School> getSchoolByName(String schoolName) {
        return schoolRepository.findByName(schoolName);
    }


}
