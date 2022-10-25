package com.quiddle.quiddleApplication.controllers.auth;

import com.quiddle.quiddleApplication.dto.UserDto;
import com.quiddle.quiddleApplication.events.OnRegistrationCompleteEvent;
import com.quiddle.quiddleApplication.models.School;
import com.quiddle.quiddleApplication.models.User;
import com.quiddle.quiddleApplication.models.VerificationToken;
import com.quiddle.quiddleApplication.requests.CreateUserRequest;
import com.quiddle.quiddleApplication.requests.UpdateUserCredentialRequest;
import com.quiddle.quiddleApplication.security.JwtTokenUtil;
import com.quiddle.quiddleApplication.exceptions.ApplicationException;
import com.quiddle.quiddleApplication.requests.LoginRequest;
import com.quiddle.quiddleApplication.responses.ApiResponse;
import com.quiddle.quiddleApplication.security.JwtUserDetailsService;
import com.quiddle.quiddleApplication.services.RoleService;
import com.quiddle.quiddleApplication.services.SchoolService;
import com.quiddle.quiddleApplication.services.UserService;
import com.quiddle.quiddleApplication.services.VerificationTokenService;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("auth")
public class AuthController {

    public final static String DEFAULT_TOKEN_KEY = "token";

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    private final JwtUserDetailsService userDetailsService;

    private final UserService userService;

    private final VerificationTokenService verificationTokenService;

    private final RoleService roleService;

    private final SchoolService schoolService;

    private final PasswordEncoder passwordEncoder;

    private final ApplicationEventPublisher eventPublisher;

    public AuthController(
            AuthenticationManager authenticationManager,
            JwtTokenUtil jwtTokenUtil,
            JwtUserDetailsService userDetailsService,
            UserService userService,
            VerificationTokenService verificationTokenService,
            RoleService roleService,
            SchoolService schoolService,
            PasswordEncoder passwordEncoder,
            ApplicationEventPublisher eventPublisher
    ) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
        this.verificationTokenService = verificationTokenService;
        this.roleService = roleService;
        this.schoolService = schoolService;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }

    @ApiOperation(
            value = "${api.auth.get-my-user-details.description}",
            notes = "${api.auth.get-my-user-details.notes}"
    )
    @GetMapping(value = "/get-my-user-details")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<?> getMyUserDetails(@AuthenticationPrincipal User user){
        return ResponseEntity.ok(new ApiResponse(user, "Get user details successful", ApiResponse.SUCCESS));
    }

    @ApiOperation(
            value = "${api.auth.login.description}",
            notes = "${api.auth.login.notes}"
    )
    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest loginRequest) throws ApplicationException {

        authenticate(loginRequest.getUsername(), loginRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(loginRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new ApiResponse(Map.of(DEFAULT_TOKEN_KEY, token), "login successful", ApiResponse.SUCCESS));
    }

    @ApiOperation(
            value = "${api.auth.register.description}",
            notes = "${api.auth.register.notes}"
    )

    @PostMapping(value = "/register")
    public ResponseEntity<?> register(@RequestBody @Valid CreateUserRequest createUserRequest) throws ApplicationException {
        Long schoolId = createUserRequest.getSchoolId();
        School school =  schoolService.getSchoolById(schoolId).orElseThrow(()-> new ApplicationException(String.format("School id [%d] does not exist", schoolId)));

        User userObj = UserDto.fromCreateUserRequest(
                createUserRequest,
                roleService.getDefaultRole().orElse(null),
                school,
                passwordEncoder
        );

        if(userService.checkUserExist(userObj)) {
            throw new ApplicationException("Username or email already exists");
        }

        User user = userService.createUser(userObj);

        String appUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, appUrl));

        return ResponseEntity.ok(new ApiResponse(user, "User created successfully. A verification mail has been sent your email, kindly check to verify your account ", ApiResponse.SUCCESS));
    }

    @ApiOperation(
            value = "${api.auth.verify.description}",
            notes = "${api.auth.verify.notes}"
    )
    @GetMapping("/verify")
    public ResponseEntity<ApiResponse> confirmRegistration(@RequestParam("token") String token) throws ApplicationException {
        VerificationToken verificationToken = verificationTokenService.getVerificationToken(token)
                                                    .orElseThrow(() -> new ApplicationException(String.format("Invalid Verification Token [%s]", token)));

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();

        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new ApplicationException("Verification token expired");
        }

        user.setEnabled(true);
        user = userService.updateUser(user);

        return ResponseEntity.ok(new ApiResponse(user, "User verified successfully", ApiResponse.SUCCESS));
    }

    @ApiOperation(
            value = "${api.auth.update-credentials.description}",
            notes = "${api.auth.update-credentials.notes}"
    )
    @PatchMapping("/update-credentials")
    @PreAuthorize("hasAuthority('can-update-credentials')")
    public ResponseEntity<ApiResponse> updateCredentials(
            @RequestBody UpdateUserCredentialRequest updateUserCredentialRequest,
            @ApiIgnore UsernamePasswordAuthenticationToken authenticationToken
    ) throws ApplicationException {
        Optional<User> userOptional = userService.getUserByUsername(updateUserCredentialRequest.getUsername());

        User authenticatedUser = ((User)authenticationToken.getPrincipal());

        if (userOptional.isPresent()){
            throw new ApplicationException("Username already exists");
        }

        if( authenticatedUser.getUsername().equals(updateUserCredentialRequest.getUsername()) ){
            throw new ApplicationException("You cannot change your credentials to your previous credentials");
        }

        User user = UserDto.fromUpdateUserRequest(
                updateUserCredentialRequest,
                authenticatedUser,
                passwordEncoder
        );

        user = userService.updateUser(user);

        return ResponseEntity.ok(new ApiResponse(user, "User credentials updated successfully", ApiResponse.SUCCESS));
    }

    private void authenticate(String username, String password) throws ApplicationException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new ApplicationException("User has not been validated, check email to complete registration");
        } catch (BadCredentialsException e) {
            throw new ApplicationException("Incorrect Credentials Provided");
        }
    }
}
