package com.quiddle.quiddleApplication.dto;

import com.quiddle.quiddleApplication.models.User;
import com.quiddle.quiddleApplication.models.VerificationToken;

public class VerificationDto {
    public static VerificationToken getValidationToken(String token, User user) {
        return VerificationToken.builder()
                .user(user)
                .expiryDate(VerificationToken.calculateExpiryDate(VerificationToken.EXPIRATION))
                .token(token)
                .build();
    }
}
