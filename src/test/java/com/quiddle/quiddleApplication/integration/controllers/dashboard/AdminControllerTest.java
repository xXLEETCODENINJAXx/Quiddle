package com.quiddle.quiddleApplication.integration.controllers.dashboard;

import com.quiddle.quiddleApplication.constants.AppConstants;
import com.quiddle.quiddleApplication.enums.ERole;
import com.quiddle.quiddleApplication.models.Role;
import com.quiddle.quiddleApplication.models.School;
import com.quiddle.quiddleApplication.models.User;
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
class AdminControllerTest {

    private static final String ASSIGN_ROLE_TO_USER_URL = "/admin/assign-role/users/%s/roles/%s";
    private static final String ASSIGN_ADMIN_ROLE_TO_USER_URL = "/admin/assign-admin-role/users/%s";
    private static final int SCHOOL_INDEX_1 = 1;
    private static final int SCHOOL_INDEX_2 = 2;
    private static final String ADD_TEACHER_TO_SCHOOL_URL = "/admin/add-teacher-to-school/teachers/%s";
    private static final String REMOVE_TEACHER_FROM_SCHOOL = "/admin/remove-teacher-from-school/teachers/%s";
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private FakeDataService fakeDataService;

    @Autowired
    private MockMvc mvc;

    private User admin;

    private User teacher;

    private User superAdmin;
    private Role adminRole;

    private List<School> schools;

    @BeforeEach
    void setUp() {
        // Setup School
        schools = schoolRepository.findAll();
        assertFalse(schools.isEmpty());
        assertEquals(schools.size(), AppConstants.DEFAULT_SCHOOLS_CLASSES.size());

        School school = schools.get(SCHOOL_INDEX_1);

        // Get Teacher and Student Role
        Optional<Role> superAdminRoleOptional = roleRepository.findByName(ERole.SUPER_ADMIN.name());
        assertTrue(superAdminRoleOptional.isPresent());

        Optional<Role> adminRoleOptional = roleRepository.findByName(ERole.ADMIN.name());
        assertTrue(adminRoleOptional.isPresent());
        adminRole = adminRoleOptional.get();

        Optional<Role> teacherRoleOptional = roleRepository.findByName(ERole.TEACHER.name());
        assertTrue(teacherRoleOptional.isPresent());

        // Setup Super Admin Account
        User superAdmin = fakeDataService.getUser();
        superAdmin.setRole(superAdminRoleOptional.get());

        // Setup Admin Account
        User admin = fakeDataService.getUser();
        admin.setSchool(school);
        admin.setRole(adminRoleOptional.get());

        // Setup Teacher Account
        User teacher = fakeDataService.getUser();
        teacher.setSchool(school);
        teacher.setRole(teacherRoleOptional.get());

        this.superAdmin = userRepository.save(superAdmin);
        this.admin = userRepository.save(admin);
        this.teacher = userRepository.save(teacher);
    }

    @Test
    void assignAdminRoleToUserBySuperAdmin() throws Exception {
        superAdmin.setAuthorities(List.of(new SimpleGrantedAuthority(ERole.SUPER_ADMIN.name())));

        //TEST
        mvc.perform(
                        patch(String.format(ASSIGN_ADMIN_ROLE_TO_USER_URL, teacher.getId()))
                                .with(user(superAdmin))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<User> teacherOptional = userRepository.findById(teacher.getId());
        assertTrue(teacherOptional.isPresent());

        String expectedTeacherRoleAfterChangedToAdmin = ERole.ADMIN.name();

        assertEquals(expectedTeacherRoleAfterChangedToAdmin, teacherOptional.get().getRole().getName());
    }

    @Test
    void assignRoleToUserByAdmin() throws Exception {
        admin.setAuthorities(List.of(new SimpleGrantedAuthority(ERole.ADMIN.name())));
        //TEST
        mvc.perform(
                        patch(String.format(ASSIGN_ROLE_TO_USER_URL, teacher.getId(), adminRole.getId()))
                                .with(user(admin))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<User> teacherOptional = userRepository.findById(teacher.getId());
        assertTrue(teacherOptional.isPresent());

        String expectedTeacherRoleAfterChangedToAdmin = ERole.ADMIN.name();

        assertEquals(expectedTeacherRoleAfterChangedToAdmin, teacherOptional.get().getRole().getName());
    }

    @Test
    void addTeacherToSchool() throws Exception {
        admin.setAuthorities(List.of(new SimpleGrantedAuthority(ERole.ADMIN.name())));

        teacher.setSchool(schools.get(SCHOOL_INDEX_2));
        teacher = userRepository.save(teacher);

        //TEST
        mvc.perform(
                        patch(String.format(ADD_TEACHER_TO_SCHOOL_URL, teacher.getId()))
                                .with(user(admin))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<User> teacherOptional = userRepository.findById(teacher.getId());
        assertTrue(teacherOptional.isPresent());

        Long expectedTeacherSchoolId = admin.getSchool().getId();
        //TEST
        assertNotNull(teacherOptional.get().getSchool());
        assertEquals(expectedTeacherSchoolId, teacherOptional.get().getSchool().getId());
    }

    @Test
    void removeTeacherFromSchool() throws Exception {
        admin.setAuthorities(List.of(new SimpleGrantedAuthority(ERole.ADMIN.name())));

        //TEST
        mvc.perform(
                        patch(String.format(REMOVE_TEACHER_FROM_SCHOOL, teacher.getId()))
                                .with(user(admin))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Optional<User> teacherOptional = userRepository.findById(teacher.getId());
        assertTrue(teacherOptional.isPresent());

        //TEST
        assertNull(teacherOptional.get().getSchool());
    }
}