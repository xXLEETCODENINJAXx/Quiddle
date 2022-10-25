package com.quiddle.quiddleApplication.requests;

import com.quiddle.quiddleApplication.requests.constraints.Password;
import lombok.Data;


@Data
public class UpdateUserCredentialRequest {

    public String username;

    @Password(nullable = true)
    public String password;
}
