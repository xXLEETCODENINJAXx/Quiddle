package com.quiddle.quiddleApplication.controllers;

import com.quiddle.quiddleApplication.models.Class;
import com.quiddle.quiddleApplication.models.School;
import com.quiddle.quiddleApplication.models.User;
import com.quiddle.quiddleApplication.responses.ApiResponse;
import com.quiddle.quiddleApplication.services.ClassService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("school/classes")
@PreAuthorize("hasAuthority('STUDENT')")
public class ClassController {

    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @ApiOperation(
            value = "${api.auth.get-all-school-classes.description}",
            notes = "${api.auth.get-all-school-classes.notes}"
    )
    @GetMapping
    public ResponseEntity<ApiResponse> allSchoolClasses(@AuthenticationPrincipal User user){
        School school = user.getSchool();

        List<Class> classes = classService.getAllClassesBySchool(school);

        return new ResponseEntity<>(new ApiResponse(classes, "Your school classes retrieved successfully", ApiResponse.SUCCESS), HttpStatus.OK);
    }
}
