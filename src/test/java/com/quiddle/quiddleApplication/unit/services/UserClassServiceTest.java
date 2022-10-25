package com.quiddle.quiddleApplication.unit.services;

import com.quiddle.quiddleApplication.models.Class;
import com.quiddle.quiddleApplication.models.User;
import com.quiddle.quiddleApplication.models.UserClass;
import com.quiddle.quiddleApplication.repositories.UserClassRepository;
import com.quiddle.quiddleApplication.services.FakeDataService;
import com.quiddle.quiddleApplication.services.UserClassService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserClassServiceTest {

    @MockBean
    private UserClassRepository userClassRepository;

    @Autowired
    private UserClassService userClassService;

    @Autowired
    private FakeDataService fakeDataService;

    @Test
    void addUserToClass() {
        //GIVEN
        UserClass userClass = fakeDataService.getUserClass();
        Long expectedUserClassId = 1L;
//        userClass.setId(expectedUserClassId);

        //MOCK
        when(userClassRepository.save(any(UserClass.class))).thenReturn(userClass);

        userClass = userClassService.addUserToClass(fakeDataService.getUserClass());

        //TEST
        assertNotNull(userClass);
//        assertEquals(expectedUserClassId, userClass.getId());
    }

    @Test
    void removeUserFromClass() {
        //GIVEN
        UserClass userClass = fakeDataService.getUserClass();
        userClassService.removeUserFromClass(userClass);

        //TEST
        verify(userClassRepository, times(1)).delete(any(UserClass.class));
    }

    @Test
    void getUserClassByUserIdAndClassId() {
        //GIVEN
        UserClass userClass = fakeDataService.getUserClass();
        Long expectedUserClassId = 1L;
//        userClass.setId(expectedUserClassId);

        User user = fakeDataService.getUser();
        Class defaultClass = fakeDataService.getClassObj();

        //MOCK
        when(userClassRepository.findOne(any(Example.class)))
                .thenReturn(Optional.of(userClass));

        Optional<UserClass> userClassOptional = userClassService.getUserClassByUserIdAndClassId(user.getId(),  defaultClass.getId());

        //TEST
        assertTrue(userClassOptional.isPresent());
//        assertEquals(expectedUserClassId, userClassOptional.get().getId());
    }
}