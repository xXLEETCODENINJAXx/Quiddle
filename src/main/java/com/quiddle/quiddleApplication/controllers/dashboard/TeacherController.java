package com.quiddle.quiddleApplication.controllers.dashboard;

import com.quiddle.quiddleApplication.dto.UserClassDto;
import com.quiddle.quiddleApplication.enums.ERole;
import com.quiddle.quiddleApplication.exceptions.ApplicationException;
import com.quiddle.quiddleApplication.models.School;
import com.quiddle.quiddleApplication.models.User;
import com.quiddle.quiddleApplication.models.UserClass;
import com.quiddle.quiddleApplication.responses.ApiResponse;
import com.quiddle.quiddleApplication.services.ClassService;
import com.quiddle.quiddleApplication.services.UserClassService;
import com.quiddle.quiddleApplication.services.UserSchoolService;
import com.quiddle.quiddleApplication.services.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(path = "/teachers")
@PreAuthorize("hasAuthority('TEACHER')")
public class TeacherController {

    private final UserService userService;
    private final ClassService classService;
    private final UserClassService userClassService;
    private final UserSchoolService userSchoolService;

    public TeacherController(
            UserService userService,
            UserSchoolService userSchoolService,
            ClassService classService,
            UserClassService userClassService
    ) {
        this.userService = userService;
        this.classService = classService;
        this.userClassService = userClassService;
        this.userSchoolService = userSchoolService;
    }

    @ApiOperation(
            value = "${api.auth.add-student-to-class.description}",
            notes = "${api.auth.add-student-to-class.notes}"
    )
    @PatchMapping("add-student-to-class/students/{studentId}/classes/{classId}")
    public ResponseEntity<ApiResponse> addStudentToClass(
            @PathVariable("studentId") Long studentId,
            @PathVariable("classId") Long classId,
            @ApiIgnore UsernamePasswordAuthenticationToken authenticationToken
    ) throws ApplicationException {
        User student = getStudent(studentId);

        checkIfAuthorised((User)authenticationToken.getPrincipal(), student);

        if (userClassService.getUserClassByUserIdAndClassId(studentId, classId).isPresent()){
            throw new ApplicationException(String.format("Student with id %d is already a member of class", studentId)) ;
        }

        UserClass userClass = UserClassDto.getUserClass(studentId, classId);

        userClassService.addUserToClass(userClass);

        return ResponseEntity.ok(new ApiResponse(student, "Student added to class successfully", ApiResponse.SUCCESS));
    }

    @ApiOperation(
            value = "${api.auth.remove-student-from-class.description}",
            notes = "${api.auth.remove-student-from-class.notes}"
    )
    @PatchMapping("remove-student-from-class/students/{studentId}/classes/{classId}")
    public ResponseEntity<ApiResponse> removeStudentFromClass(
            @PathVariable("studentId") Long studentId,
            @PathVariable("classId") Long classId,
            @ApiIgnore UsernamePasswordAuthenticationToken authenticationToken
    ) throws ApplicationException {
        User student = getStudent(studentId);

        checkIfAuthorised((User)authenticationToken.getPrincipal(), student);

        UserClass userClass = userClassService.getUserClassByUserIdAndClassId(studentId, classId).orElseThrow(()-> new ApplicationException("Student does not belong to class"));

        userClassService.removeUserFromClass(userClass);

        return ResponseEntity.ok(new ApiResponse(student, "Student removed from class successfully", ApiResponse.SUCCESS));
    }

    private User getStudent(Long studentId) throws ApplicationException {
        User user = userService.getUserById(studentId).orElseThrow(() -> new ApplicationException(String.format("Student with id %d does not exist", studentId)));

        if( userService.isType(ERole.STUDENT, user) ){
            return user;
        }

        throw new ApplicationException(String.format("User with id %d is not a student", studentId)) ;
    }

    private void checkIfAuthorised(
            User authenticatedUser,
            User user
    ) throws ApplicationException {
        School school = authenticatedUser.getSchool();

        if( !userSchoolService.userBelongsToSchool(user, school)){
            throw new ApplicationException("Unauthorised request, Ensure student belongs to your school");
        }
    }
}
