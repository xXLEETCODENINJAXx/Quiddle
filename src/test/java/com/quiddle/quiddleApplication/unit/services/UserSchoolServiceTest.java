package com.quiddle.quiddleApplication.unit.services;

import com.quiddle.quiddleApplication.models.School;
import com.quiddle.quiddleApplication.models.User;
import com.quiddle.quiddleApplication.repositories.UserRepository;
import com.quiddle.quiddleApplication.services.FakeDataService;
import com.quiddle.quiddleApplication.services.UserSchoolService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserSchoolServiceTest {

    private static final Long TEST_SCHOOL_ID_1 = 1L;
    private static final Long TEST_SCHOOL_ID_2 = 2L;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserSchoolService userSchoolService;

    @Autowired
    private FakeDataService fakeDataService;

    @Test
    void addUserToSchool_Successful() {
        School school = fakeDataService.getSchool();
        User user = fakeDataService.getUser();
        user.setSchool(school);

        when(userRepository.save(any(User.class))).thenReturn(user);

        user =  userSchoolService.addUserToSchool(fakeDataService.getUser(), fakeDataService.getSchool());

        Long expectedSchoolId = school.getId();

        assertNotNull(user.getSchool());
        assertEquals(expectedSchoolId, user.getSchool().getId());
    }

    @Test
    void removeUserFromSchool_Successful() {
        School school = fakeDataService.getSchool();
        User user = fakeDataService.getUser();
        user.setSchool(school);

        when(userRepository.save(any(User.class))).thenReturn(fakeDataService.getUser());

        userSchoolService.removeUserFromSchool(user, school);

        assertNull(user.getSchool());
    }

    @Test
    void userBelongsToSchool_WhenUserBelongsToSchool_ReturnsTrue() {
        School school = fakeDataService.getSchool();
        User user = fakeDataService.getUser();
        user.setSchool(school);

        boolean userBelongsToSchool =  userSchoolService.userBelongsToSchool(user, school);

        assertTrue(userBelongsToSchool);
    }

    @Test
    void userBelongsToSchool_WhenUserDoesNotBelongToSchool_ReturnsFalse() {
        School school = fakeDataService.getSchool();
        school.setId(TEST_SCHOOL_ID_1);
        User user = fakeDataService.getUser();
        user.setSchool(school);

        School schoolOne = fakeDataService.getSchool();
        schoolOne.setId(TEST_SCHOOL_ID_2);

        boolean userBelongsToSchool =  userSchoolService.userBelongsToSchool(user, schoolOne);

        assertFalse(userBelongsToSchool);
    }
}