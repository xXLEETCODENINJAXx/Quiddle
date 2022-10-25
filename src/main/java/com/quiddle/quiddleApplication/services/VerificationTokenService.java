package com.quiddle.quiddleApplication.services;

import com.quiddle.quiddleApplication.models.VerificationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface VerificationTokenService {

    Optional<VerificationToken> getVerificationToken(String token) ;

    VerificationToken createVerificationToken(VerificationToken token);
}
