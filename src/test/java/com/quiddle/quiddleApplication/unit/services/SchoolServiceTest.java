package com.quiddle.quiddleApplication.unit.services;

import com.quiddle.quiddleApplication.exceptions.ApplicationException;
import com.quiddle.quiddleApplication.models.School;
import com.quiddle.quiddleApplication.repositories.SchoolRepository;
import com.quiddle.quiddleApplication.services.FakeDataService;
import com.quiddle.quiddleApplication.services.SchoolService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class SchoolServiceTest  {

    @Autowired
    private SchoolService schoolService;

    @MockBean
    private SchoolRepository schoolRepository;

    @Autowired
    private FakeDataService fakeDataService;

    @Test
    void create_Successful() {
        //GIVEN
        School school = fakeDataService.getSchool();
        String expectedSchoolName = school.getName();

        //MOCK
        when(schoolRepository.save(any(School.class))).thenReturn(school);

        school = schoolService.create(school);

        //TEST
        assertNotNull(school);
        assertEquals(expectedSchoolName, school.getName());
    }

    @Test
    void getAllSchools() {
        //GIVEN
        List<School> schools = List.of(fakeDataService.getSchool());

        //MOCK
        when(schoolRepository.findAll()).thenReturn(schools);

        schools = schoolService.getAllSchools();

        int expectedNumberOfSchools = 1;
        //TEST
        assertEquals(expectedNumberOfSchools, schools.size());
    }

    @Test
    void getSchoolById_WhenIdExist_Successful() throws ApplicationException {
        //GIVEN
        School school = fakeDataService.getSchool();
        Long expectedSchoolId = 1L;
        school.setId(expectedSchoolId);

        //MOCK
        when(schoolRepository.findById(any(Long.class))).thenReturn(Optional.of(school));

        Optional<School> schoolOptional = schoolService.getSchoolById(school.getId());

        //TEST
        assertTrue(schoolOptional.isPresent());
        assertEquals(expectedSchoolId, school.getId());
    }

    @Test
    void getSchoolById_WhenIdDoesNotExist_ReturnsOptionalOfNull() throws ApplicationException {
        //MOCK
        when(schoolRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        //GIVEN
        Long nonExistingId = 1000L;

        Optional<School> schoolOptional = schoolService.getSchoolById(nonExistingId);

        //TEST
        assertFalse(schoolOptional.isPresent());
    }

    @Test
    void getSchoolByName_WhenNameExist_Successful() {
        //GIVEN
        School school = fakeDataService.getSchool();
        Long expectedSchoolId = 1L;
        school.setId(expectedSchoolId);

        //MOCK
        when(schoolRepository.findByName(any(String.class))).thenReturn(Optional.of(school));

        Optional<School> schoolOptional = schoolService.getSchoolByName(school.getName());

        //TEST
        assertTrue(schoolOptional.isPresent());
        assertEquals(expectedSchoolId, school.getId());
    }

    @Test
    void getSchoolByName_WhenNameDoesNotExist_ReturnsOptionalOfNull() {
        //MOCK
        when(schoolRepository.findByName(any(String.class))).thenReturn(Optional.empty());

        //GIVEN
        String nonExistingName = "Non Existing Name";

        Optional<School> schoolOptional = schoolService.getSchoolByName(nonExistingName);

        //TEST
        assertFalse(schoolOptional.isPresent());
    }
}