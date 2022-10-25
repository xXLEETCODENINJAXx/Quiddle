package com.quiddle.quiddleApplication.services;

import com.quiddle.quiddleApplication.models.School;
import com.quiddle.quiddleApplication.models.Class;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClassService {
     Class create(Class classObj);

    List<Class> getAllClassesBySchool(School school);
}
