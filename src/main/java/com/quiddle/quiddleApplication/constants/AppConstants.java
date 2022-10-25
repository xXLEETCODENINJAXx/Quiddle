package com.quiddle.quiddleApplication.constants;

import com.quiddle.quiddleApplication.enums.ERole;

import java.util.Map;

import static com.quiddle.quiddleApplication.constants.PermissionConstants.*;
import static java.util.Map.entry;

public interface AppConstants {

    Map<ERole, String[]> DEFAULT_ROLES_PERMISSIONS = Map.ofEntries(
        entry(ERole.ADMIN, new String[]{CAN_REMOVE_TEACHER_FROM_SCHOOL, CAN_ADD_TEACHER_TO_SCHOOL}),
        entry(ERole.TEACHER, new String[]{CAN_ADD_STUDENT_TO_CLASS, CAN_REMOVE_STUDENT_FROM_CLASS}),
        entry(ERole.STUDENT, new String[]{}),
        entry(ERole.SUPER_ADMIN, new String[]{}),
        entry(ERole.USER, new String[]{CAN_UPDATE_CREDENTIALS})
    );

    Map<String, String[]> DEFAULT_SCHOOLS_CLASSES = Map.ofEntries(
        entry("School 1", new String[]{"Class 1", "Class 2", "Class 3"}),
        entry("School 2", new String[]{"Class 4", "Class 5", "Class 6"}),
        entry("School 3", new String[]{"Class 1", "Class 2", "Class 3", "Class 4", "Class 5", "Class 6"})
    );
}
