package com.quiddle.quiddleApplication;

import com.quiddle.quiddleApplication.constants.AppConstants;
import com.quiddle.quiddleApplication.dto.*;
import com.quiddle.quiddleApplication.enums.ERole;
import com.quiddle.quiddleApplication.exceptions.ApplicationException;
import com.quiddle.quiddleApplication.models.Role;
import com.quiddle.quiddleApplication.models.School;
import com.quiddle.quiddleApplication.models.User;
import com.quiddle.quiddleApplication.requests.CreateUserRequest;
import com.quiddle.quiddleApplication.services.*;
import com.quiddle.quiddleApplication.dto.*;
import com.quiddle.quiddleApplication.services.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Map;

@EnableAsync
@SpringBootApplication
public class SchoolServiceAppApplication implements CommandLineRunner {

    private final SchoolService schoolService;
    private final ClassService classService;

    private final RoleService roleService;

    private final PermissionService permissionService;

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.superadmin.username}")
    private String superAdminUsername;

    @Value("${app.superadmin.password}")
    private String superAdminPassword;

    @Value("${app.superadmin.email}")
    private String superAdminEmail;

    public SchoolServiceAppApplication(
            SchoolService schoolService,
            ClassService classService,
            RoleService roleService,
            PermissionService permissionService,
            UserService userService,
            PasswordEncoder passwordEncoder
    ) {
        this.schoolService = schoolService;
        this.classService = classService;
        this.roleService = roleService;
        this.permissionService = permissionService;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args) {
        SpringApplication.run(SchoolServiceAppApplication.class, args);
    }

    @Override
    public void run(String... args) throws ApplicationException {
        createDefaultSchoolsAndClassesIfNotExist();
        createDefaultRolesAndPermissionsIfNotExist();
        createSuperAdminIfNotExist();
    }

    @Transactional
     void createSuperAdminIfNotExist() throws ApplicationException {
        CreateUserRequest createUserRequest = CreateUserRequest.builder()
                                                .username(superAdminUsername)
                                                .password(superAdminPassword)
                                                .email(superAdminEmail)
                                                .build();

        Role superAdminRole = roleService.getRole(ERole.SUPER_ADMIN).orElse(null );

        User superAdmin = UserDto.fromCreateUserRequest(createUserRequest, superAdminRole, null, passwordEncoder);
        userService.getUserByUsername(superAdmin.getUsername()).orElse(userService.createUser(superAdmin));
    }

    @Transactional
    void createDefaultRolesAndPermissionsIfNotExist() throws ApplicationException {
        for (Map.Entry<ERole, String[]> rolePermissionsEntry : AppConstants.DEFAULT_ROLES_PERMISSIONS.entrySet()) {
            ERole roleEnum = rolePermissionsEntry.getKey();

            if(roleService.getRole(roleEnum).isEmpty()){
                Role role = RoleDto.getRole(roleEnum);
                String[] permissions = rolePermissionsEntry.getValue();

                Arrays.stream(permissions).forEach(permissionName -> {
                    role.getPermissions().add(PermissionDto.getPermission(permissionName));
                });

                roleService.create(role);
            }
        }
    }

    @Transactional
    void createDefaultSchoolsAndClassesIfNotExist() {
        for (Map.Entry<String, String[]> schoolClassesEntry : AppConstants.DEFAULT_SCHOOLS_CLASSES.entrySet()) {
            String schoolName = schoolClassesEntry.getKey();
            String[] classes = schoolClassesEntry.getValue();

            if(schoolService.getSchoolByName(schoolName).isEmpty()){
                School school = schoolService.create(SchoolDto.getSchool(schoolName));

                Arrays.stream(classes).forEach((name) -> {
                    classService.create(ClassDto.getClass(name, school));
                });
            }
        }
    }
}
