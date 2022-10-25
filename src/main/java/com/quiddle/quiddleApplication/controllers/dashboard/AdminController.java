package com.quiddle.quiddleApplication.controllers.dashboard;

import com.quiddle.quiddleApplication.enums.ERole;
import com.quiddle.quiddleApplication.exceptions.ApplicationException;
import com.quiddle.quiddleApplication.models.Role;
import com.quiddle.quiddleApplication.models.School;
import com.quiddle.quiddleApplication.models.User;
import com.quiddle.quiddleApplication.responses.ApiResponse;
import com.quiddle.quiddleApplication.services.*;
import com.quiddle.quiddleApplication.services.RoleService;
import com.quiddle.quiddleApplication.services.UserRoleService;
import com.quiddle.quiddleApplication.services.UserSchoolService;
import com.quiddle.quiddleApplication.services.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(path = "/admin")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {

    public final UserService userService;
    public final RoleService roleService;

    public final UserSchoolService userSchoolService;
    public final UserRoleService userRoleService;

    public AdminController(
            UserService userService,
            RoleService roleService,
            UserSchoolService userSchoolService,
            UserRoleService userRoleService
    ) {
        this.userService = userService;
        this.roleService = roleService;
        this.userSchoolService = userSchoolService;
        this.userRoleService = userRoleService;
    }

    @ApiOperation(
            value = "${api.auth.assign-admin-role-to-user.description}",
            notes = "${api.auth.assign-admin-role-to-user.notes}"
    )
    @PatchMapping("assign-admin-role/users/{userId}")
    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse> assignAdminRoleToUser(@PathVariable("userId") Long userId) throws ApplicationException {
        User user = userService.getUserById(userId).orElseThrow(() -> new ApplicationException(String.format("User with id %d does not exist", userId)));
        Role role = roleService.getRole(ERole.ADMIN).orElseThrow(()-> new ApplicationException(String.format("Role with name %s does not exist", ERole.ADMIN.name())));;

        userRoleService.assignRoleToUser(role, user);

        return new ResponseEntity<>(new ApiResponse(null, "User made an admin successfully", ApiResponse.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(
            value = "${api.auth.assign-role-to-user.description}",
            notes = "${api.auth.assign-role-to-user.notes}"
    )
    @PatchMapping("assign-role/users/{userId}/roles/{roleId}")
    public ResponseEntity<ApiResponse> assignRoleToUser(
            @PathVariable("userId") Long userId,
            @PathVariable("roleId") Long roleId,
            @ApiIgnore UsernamePasswordAuthenticationToken authenticationToken
    ) throws ApplicationException {
        User user = userService.getUserById(userId).orElseThrow(() -> new ApplicationException(String.format("User with id %d does not exist", userId)));
        User authenticatedUser = (User)authenticationToken.getPrincipal();

        checkIfAuthorised(authenticatedUser, user);

        if(user.equals(authenticatedUser) ){
            throw new ApplicationException("You cannot assign yourself a role");
        }

        Role role = roleService.getRoleById(roleId).orElseThrow(()-> new ApplicationException(String.format("Role with id %s does not exist", roleId)));;
        String roleName = role.getName();

        if(roleName.equals(ERole.SUPER_ADMIN.name())){
            throw new ApplicationException("You are not authorised to make a user a role of super admin");
        }

        userRoleService.assignRoleToUser(role, user);

        return new ResponseEntity<>(new ApiResponse(null, String.format("User assigned role [%s] successfully", roleName), ApiResponse.SUCCESS), HttpStatus.OK);
    }

    @ApiOperation(
            value = "${api.auth.add-teacher-to-school.description}",
            notes = "${api.auth.add-teacher-to-school.notes}"
    )
    @PatchMapping("add-teacher-to-school/teachers/{teacherId}")
    public ResponseEntity<ApiResponse> addTeacherToSchool(
            @PathVariable("teacherId") Long teacherId,
            @ApiIgnore UsernamePasswordAuthenticationToken authenticationToken
    ) throws ApplicationException {
        User teacher = getTeacher(teacherId);
        School school = ((User)authenticationToken.getPrincipal()).getSchool();

        userSchoolService.addUserToSchool(teacher, school);

        return ResponseEntity.ok(new ApiResponse(teacher, "Teacher added to school successfully", ApiResponse.SUCCESS));
    }

    @ApiOperation(
            value = "${api.auth.remove-teacher-from-school.description}",
            notes = "${api.auth.remove-teacher-from-school.notes}"
    )
    @PatchMapping("remove-teacher-from-school/teachers/{teacherId}")
    public ResponseEntity<ApiResponse> removeTeacherFromSchool(
            @PathVariable("teacherId") Long teacherId,
            @ApiIgnore UsernamePasswordAuthenticationToken authenticationToken
    ) throws ApplicationException {
        User teacher = getTeacher(teacherId);

        School school = getSchoolIfAuthorised((User)authenticationToken.getPrincipal(), teacher);

        userSchoolService.removeUserFromSchool(teacher, school);

        return ResponseEntity.ok(new ApiResponse(teacher, "Teacher removed from school successfully", ApiResponse.SUCCESS));
    }

    private User getTeacher(Long teacherId) throws ApplicationException {
        User user = userService.getUserById(teacherId).orElseThrow(() -> new ApplicationException(String.format("Teacher with id %d does not exist", teacherId)));

        if( userService.isType(ERole.TEACHER, user) ){
            return user;
        }

        throw new ApplicationException(String.format("User with id %d is not a teacher", teacherId)) ;
    }

    private School getSchoolIfAuthorised(@AuthenticationPrincipal User authenticatedUser, User user) throws ApplicationException {
        School school = authenticatedUser.getSchool();

        if( userSchoolService.userBelongsToSchool(user, school)){
            return school;
        }

        throw new ApplicationException("Unauthorised Request, Ensure user belongs to your school");
    }

    private void checkIfAuthorised(User authenticatedUser, User user) throws ApplicationException {
        getSchoolIfAuthorised(authenticatedUser, user);
    }
}
