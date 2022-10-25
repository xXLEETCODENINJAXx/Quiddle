package com.quiddle.quiddleApplication.requests;

import com.quiddle.quiddleApplication.requests.constraints.Password;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateUserRequest {

    @NotEmpty(message = "Field [username] is required and cannot be empty")
    private String username;

    @NotEmpty(message = "Field [firstName] is required and cannot be empty")
    private String firstName;

    @NotEmpty(message = "Field [lastName] is required and cannot be empty")
    private String lastName;

    @NotEmpty(message = "Field [password] is required and cannot be empty")
    @Password
    private String password;

    @NotEmpty(message = "Field [email] is required and cannot be empty")
    @Email
    private String email;

    @NotNull(message = "Field [schoolId] is required")
    private Long schoolId;
}
