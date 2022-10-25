package com.quiddle.quiddleApplication.integration.controllers.dashboard;

import com.quiddle.quiddleApplication.enums.ERole;
import com.quiddle.quiddleApplication.models.Class;
import com.quiddle.quiddleApplication.models.Role;
import com.quiddle.quiddleApplication.models.School;
import com.quiddle.quiddleApplication.models.User;
import com.quiddle.quiddleApplication.repositories.ClassRepository;
import com.quiddle.quiddleApplication.repositories.RoleRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class TeacherControllerTest {

    private static final String ADD_STUDENT_TO_CLASS_URL = "/teachers/add-student-to-class/students/%s/classes/%s";
    private static final String REMOVE_STUDENT_FROM_CLASS_URL = "/teachers/remove-student-from-class/students/%s/classes/%s";
    private static final int CLASS_INDEX = 0;
    private static final int SCHOOL_INDEX = 0;
    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private FakeDataService fakeDataService;

    private User teacher;

    private User student;

    private List<Class> classes;

    @BeforeEach
    void setUp() {
        //Setup School
        List<School> schools = schoolRepository.findAll();
        assertFalse(schools.isEmpty());

        School school = schools.get(SCHOOL_INDEX);
        classes = classRepository.findAllBySchool(school);

        assertFalse(classes.isEmpty());

        // Get Teacher and Student Role
        Optional<Role> teacherRoleOptional = roleRepository.findByName(ERole.TEACHER.name());
        assertTrue(teacherRoleOptional.isPresent());
        Optional<Role> studentRoleOptional = roleRepository.findByName(ERole.STUDENT.name());
        assertTrue(studentRoleOptional.isPresent());

        // Setup Teacher Account
        User teacher = fakeDataService.getUser();
        teacher.setSchool(school);
        teacher.setRole(teacherRoleOptional.get());

        // Setup Student Account
        User student = fakeDataService.getUser();
        student.setSchool(school);
        student.setRole(studentRoleOptional.get());

        this.teacher = userRepository.save(teacher);
        this.student = userRepository.save(student);
    }

    @Test
    void addStudentToClass_Successful() throws Exception {
        teacher.setAuthorities(List.of(new SimpleGrantedAuthority(ERole.TEACHER.name())));

        //TEST
        mvc.perform(
                patch(String.format(ADD_STUDENT_TO_CLASS_URL, student.getId(), classes.get(CLASS_INDEX).getId()))
                .with(user(teacher))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void removeStudentFromClass_Successful() throws Exception {
        //GIVEN
        teacher.setAuthorities(List.of(new SimpleGrantedAuthority(ERole.TEACHER.name())));

        Class classObj = classes.get(CLASS_INDEX);

        student.getClasses().add(classObj);

        student = userRepository.save(student);

        //TEST
        mvc.perform(
                        patch(String.format(REMOVE_STUDENT_FROM_CLASS_URL, student.getId(), classObj.getId()))
                                .with(user(teacher))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}