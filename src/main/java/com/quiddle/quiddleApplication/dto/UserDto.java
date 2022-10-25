package com.quiddle.quiddleApplication.dto;

import com.quiddle.quiddleApplication.models.Role;
import com.quiddle.quiddleApplication.models.School;
import com.quiddle.quiddleApplication.models.User;
import com.quiddle.quiddleApplication.requests.CreateUserRequest;
import com.quiddle.quiddleApplication.requests.UpdateUserCredentialRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserDto {
    public static User getUser(
            String username,
            String firstname,
            String lastname,
            String password,
            String email)
    {
        return User.builder()
                .username(username)
                .firstName(firstname)
                .lastName(lastname)
                .password(password)
                .email(email)
                .build();
    }

    public static User fromCreateUserRequest(CreateUserRequest createUserRequest, Role role, School school, PasswordEncoder encoder) {

        return User.builder()
                .username(createUserRequest.getUsername())
                .firstName(createUserRequest.getFirstName())
                .lastName(createUserRequest.getLastName())
                .email(createUserRequest.getEmail())
                .password(encoder.encode(createUserRequest.getPassword()))
                .role(role)
                .school(school)
                .build();
    }

    public static User fromUpdateUserRequest(
            UpdateUserCredentialRequest updateUserCredentialRequest,
            User authenticatedUser,
            PasswordEncoder passwordEncoder
    ) {
        if(updateUserCredentialRequest.getUsername() != null ){
            authenticatedUser.setUsername(updateUserCredentialRequest.getUsername());
        }

        if(updateUserCredentialRequest.getPassword() != null){
            authenticatedUser.setPassword(passwordEncoder.encode(updateUserCredentialRequest.getPassword()));
        }

        return authenticatedUser;
    }
}
