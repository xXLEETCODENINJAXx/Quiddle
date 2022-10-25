package com.quiddle.quiddleApplication.requests;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class LoginRequest {
    @NotEmpty(message = "Field [username] is required and cannot be empty")
    private String username;

    @NotEmpty(message = "Field [password] is required and cannot be empty")
    private String password;
}
