package com.quiddle.quiddleApplication.services;

import com.emmatblingx.schoolserviceapp.models.*;
import com.quiddle.quiddleApplication.models.Class;
import com.quiddle.quiddleApplication.models.*;
import org.springframework.stereotype.Service;

@Service
public interface FakeDataService {

    User getUser();

    Role getRole();

    School getSchool();

    Class getClassObj();

    Permission getPermission();

    UserClass getUserClass();
}
