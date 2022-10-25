package com.quiddle.quiddleApplication.controllers;

import com.quiddle.quiddleApplication.models.School;
import com.quiddle.quiddleApplication.responses.ApiResponse;
import com.quiddle.quiddleApplication.services.SchoolService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("schools")
public class SchoolController {

    private final SchoolService schoolService;

    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @ApiOperation(
            value = "${api.auth.get-all-schools.description}",
            notes = "${api.auth.get-all-schools.notes}"
    )
    @GetMapping
    public ResponseEntity<ApiResponse> all(){
        List<School> schools = schoolService.getAllSchools();

        return new ResponseEntity<>(new ApiResponse(schools, "Schools retrieved successfully", ApiResponse.SUCCESS), HttpStatus.OK);
    }
}
