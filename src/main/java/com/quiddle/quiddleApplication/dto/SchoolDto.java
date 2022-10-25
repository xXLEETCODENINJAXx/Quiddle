package com.quiddle.quiddleApplication.dto;

import com.quiddle.quiddleApplication.models.School;

public class SchoolDto {
    public static School getSchool(String name)
    {
        return School.builder()
                .name(name)
                .build();
    }
}
