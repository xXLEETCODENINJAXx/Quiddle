package com.quiddle.quiddleApplication.services;

import com.quiddle.quiddleApplication.models.Class;
import com.quiddle.quiddleApplication.models.School;
import com.quiddle.quiddleApplication.repositories.ClassRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassServiceImpl implements ClassService {

    private final ClassRepository classRepository;

    public ClassServiceImpl(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    @Override
    public Class create(Class classObj) {
        return classRepository.save(classObj);
    }

    @Override
    public List<Class> getAllClassesBySchool(School school) {
        return classRepository.findAllBySchool(school);
    }
}
