package com.quiddle.quiddleApplication.integration.controllers.auth;

import com.quiddle.quiddleApplication.constants.AppConstants;
import com.quiddle.quiddleApplication.enums.ERole;
import com.quiddle.quiddleApplication.models.Role;
import com.quiddle.quiddleApplication.models.School;
import com.quiddle.quiddleApplication.models.User;
import com.quiddle.quiddleApplication.repositories.RoleRepository;
import com.quiddle.quiddleApplication.repositories.SchoolRepository;
import com.quiddle.quiddleApplication.repositories.UserRepository;
import com.quiddle.quiddleApplication.requests.LoginRequest;
import com.quiddle.quiddleApplication.services.FakeDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    private static final int SCHOOL_INDEX_0 = 0;
    private static final String LOGIN_URL = "/auth/login";
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private FakeDataService fakeDataService;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private PasswordEncoder encoder;

    private School school;
    private Role role;

    @BeforeEach
    void setUp() {
        // Setup School
        List<School> schools = schoolRepository.findAll();
        assertFalse(schools.isEmpty());
        assertEquals(schools.size(), AppConstants.DEFAULT_SCHOOLS_CLASSES.size());

        school = schools.get(SCHOOL_INDEX_0);

        Optional<Role> roleOptional = roleRepository.findByName(ERole.STUDENT.name());
        assertFalse(roleOptional.isEmpty());

        role = roleOptional.get();
    }

    @Test
    void login_WithCorrectCredentials_Successful() throws Exception {
        User user = fakeDataService.getUser();

        String password = "password";

        user.setEnabled(true);
        user.setPassword(encoder.encode(password));
        user.setRole(role);

        user = userRepository.save(user);

        LoginRequest loginRequest  = new  LoginRequest();
        loginRequest.setUsername(user.getUsername());
        loginRequest.setPassword(password);

        //TEST
        mvc.perform(
                post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRequest))
                )
                .andExpect(status().isOk());
    }
}