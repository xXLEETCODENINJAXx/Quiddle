package com.quiddle.quiddleApplication.requests.constraints;

import lombok.Data;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Data
public class PasswordValidator implements ConstraintValidator<Password, String> {
    public final static String DEFAULT_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";

    private boolean nullable;

    @Override
    public void initialize(Password password) {
        setNullable(password.nullable());
    }

    @Override
    public boolean isValid(String passwordField, ConstraintValidatorContext context) {
        if(nullable){
            return passwordField == null || passwordField.matches(DEFAULT_REGEX);
        }

        return passwordField != null && passwordField.matches(DEFAULT_REGEX);
    }
}
