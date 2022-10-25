package com.quiddle.quiddleApplication.services;

import com.quiddle.quiddleApplication.models.VerificationToken;
import com.quiddle.quiddleApplication.repositories.VerificationTokenRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    public VerificationTokenServiceImpl(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    @Override
    public Optional<VerificationToken> getVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }

    @Override
    public VerificationToken createVerificationToken(VerificationToken token) {
        return verificationTokenRepository.save(token);
    }
}
