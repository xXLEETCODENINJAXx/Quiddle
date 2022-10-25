package com.quiddle.quiddleApplication.unit.services;

import com.quiddle.quiddleApplication.models.Class;
import com.quiddle.quiddleApplication.models.School;
import com.quiddle.quiddleApplication.repositories.ClassRepository;
import com.quiddle.quiddleApplication.services.ClassService;
import com.quiddle.quiddleApplication.services.FakeDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ClassServiceTest {

    @Autowired
    private FakeDataService fakeDataService;

    @Autowired
    private ClassService classService;

    @MockBean
    private ClassRepository classRepository;

    @Test
    void create_Successful() {
        //GIVEN
        Class classObj = fakeDataService.getClassObj();
        Long expectedClassId = 1L;
        classObj.setId(expectedClassId);


        //MOCK
        when(classRepository.save(any(Class.class))).thenReturn(classObj);

        classObj = classService.create(classObj);

        //TEST
        assertNotNull(classObj);
        assertEquals(expectedClassId, classObj.getId());
    }

    @Test
    void getAllClassesBySchool() {
        //GIVEN
        Class classObj = fakeDataService.getClassObj();
        Long expectedClassId = 1L;
        classObj.setId(expectedClassId);

        List<Class> classes = List.of(classObj);

        //MOCK
        when(classRepository.findAllBySchool(any(School.class))).thenReturn(classes);

        classes = classService.getAllClassesBySchool(classObj.getSchool());

        //GIVEN
        int expectedNumberOfClients = 1;
        //TEST
        assertEquals(expectedNumberOfClients, classes.size());
        assertEquals(expectedClassId, classes.get(0).getId());
    }
}