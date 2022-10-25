package com.quiddle.quiddleApplication.dto;

import com.quiddle.quiddleApplication.models.UserClass;

public class UserClassDto {
    public static UserClass getUserClass(Long studentId, Long classId) {
        return UserClass.builder()
                .studentId(studentId)
                .classId(classId)
                .build();
    }
}
