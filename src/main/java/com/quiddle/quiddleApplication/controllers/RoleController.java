package com.quiddle.quiddleApplication.controllers;

import com.quiddle.quiddleApplication.models.Role;
import com.quiddle.quiddleApplication.responses.ApiResponse;
import com.quiddle.quiddleApplication.services.RoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @ApiOperation(
            value = "${api.auth.get-all-roles.description}",
            notes = "${api.auth.get-all-roles.notes}"
    )
    @GetMapping
    public ResponseEntity<ApiResponse> all(){
        List<Role> roles = roleService.getAllRoles();

        return new ResponseEntity<>(new ApiResponse(roles, "Roles retrieved successfully", ApiResponse.SUCCESS), HttpStatus.OK);
    }
}