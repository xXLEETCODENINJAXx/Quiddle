package com.quiddle.quiddleApplication.integration.controllers;

import com.quiddle.quiddleApplication.enums.ERole;
import com.quiddle.quiddleApplication.models.School;
import com.quiddle.quiddleApplication.models.User;
import com.quiddle.quiddleApplication.repositories.SchoolRepository;
import com.quiddle.quiddleApplication.repositories.UserRepository;
import com.quiddle.quiddleApplication.services.FakeDataService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class ClassControllerTest {

    private static final String GET_ALL_SCHOOL_CLASSES_URL = "/school/classes";
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private FakeDataService fakeDataService;

    private User authenticatedUser;

    @BeforeEach
    public void setUp(){
        List<School> schools = schoolRepository.findAll();
        assertFalse(schools.isEmpty());

        User user = fakeDataService.getUser();
        user.setSchool(schools.get(0));

        authenticatedUser = userRepository.save(user);
    }

    @Test
    void allSchoolClasses_Successful() throws Exception {
        authenticatedUser.setAuthorities(List.of(new SimpleGrantedAuthority(ERole.STUDENT.name())));

        mvc.perform(get(GET_ALL_SCHOOL_CLASSES_URL).with(user(authenticatedUser))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.*", hasSize(authenticatedUser.getSchool().getClasses().size())));
    }
}