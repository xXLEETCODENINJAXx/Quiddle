package com.quiddle.quiddleApplication.dto;

import com.quiddle.quiddleApplication.models.Class;
import com.quiddle.quiddleApplication.models.School;

public class ClassDto {
    public static Class getClass(String name, School school)
    {
        return Class.builder()
                .name(name)
                .school(school)
                .build();
    }
}
