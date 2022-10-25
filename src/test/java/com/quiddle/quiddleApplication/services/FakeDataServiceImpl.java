package com.quiddle.quiddleApplication.services;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import com.quiddle.quiddleApplication.enums.ERole;
import com.emmatblingx.schoolserviceapp.models.*;
import com.quiddle.quiddleApplication.models.Class;
import com.quiddle.quiddleApplication.models.*;
import org.springframework.stereotype.Service;

@Service
public class FakeDataServiceImpl implements FakeDataService {

    private Fairy fairy;

    public FakeDataServiceImpl() {
       fairy = Fairy.create();
    }

    @Override
    public User getUser() {
        Person person = fairy.person();

        return User.builder()
                .username(person.getUsername())
                .firstName(person.getFirstName())
                .lastName(person.getLastName())
                .email(person.getEmail())
                .password(person.getPassword())
                .build();
    }

    @Override
    public Role getRole() {
        return Role.builder()
                .name(ERole.USER.name())
                .build();
    }

    @Override
    public School getSchool() {
        return School.builder()
                .name(fairy.textProducer().word())
                .build();
    }

    @Override
    public Class getClassObj() {
        School school = School.builder()
                .name(fairy.textProducer().word())
                .build();

        return Class.builder()
                .name(fairy.textProducer().word())
                .school(school)
                .build();
    }

    @Override
    public Permission getPermission() {
        return Permission.builder()
                .name(fairy.textProducer().word())
                .build();
    }

    @Override
    public UserClass getUserClass() {
        Long studentId = 1L;
        Long classId = 1L;

        return UserClass.builder()
                .studentId(studentId)
                .classId(classId)
                .build();
    }
}
