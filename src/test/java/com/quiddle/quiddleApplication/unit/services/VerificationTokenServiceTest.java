package com.quiddle.quiddleApplication.unit.services;

import com.quiddle.quiddleApplication.models.User;
import com.quiddle.quiddleApplication.models.VerificationToken;
import com.quiddle.quiddleApplication.repositories.VerificationTokenRepository;
import com.quiddle.quiddleApplication.services.VerificationTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class VerificationTokenServiceTest {
    private static final String VERIFICATION_TOKEN = "TEST TOKEN";
    @Autowired
    private VerificationTokenService verificationTokenService;

    @MockBean
    private VerificationTokenRepository verificationTokenRepository;

    @Test
    void getVerificationToken_Successful() {
        //GIVEN
        VerificationToken verificationToken = getDefaultVerificationToken();

        //MOCK
        when(verificationTokenRepository.findByToken(any(String.class))).thenReturn(Optional.of(verificationToken));

        Optional<VerificationToken> verificationTokenOptional = verificationTokenService.getVerificationToken(verificationToken.getToken());

        String expectedTokenName = VERIFICATION_TOKEN;
        //TEST
        assertTrue(verificationTokenOptional.isPresent());
        assertEquals(expectedTokenName, verificationTokenOptional.get().getToken());
    }

    @Test
    void getVerificationToken_WhenTokenDoesNotExist_ReturnsOptionalOfNull() {
        //MOCK
        when(verificationTokenRepository.findByToken(any(String.class))).thenReturn(Optional.empty());

        String nonExistingToken = "Non Existing Token";

        Optional<VerificationToken> verificationTokenOptional = verificationTokenService.getVerificationToken(nonExistingToken);

        //TEST
        assertFalse(verificationTokenOptional.isPresent());
    }

    @Test
    void createVerificationToken() {
        //GIVEN
        VerificationToken verificationToken = getDefaultVerificationToken();

        //MOCK
        when(verificationTokenRepository.save(any(VerificationToken.class))).thenReturn(verificationToken);

        verificationToken = verificationTokenService.createVerificationToken(verificationToken);

        //TEST
        assertNotNull(verificationToken);
        assertEquals(VERIFICATION_TOKEN, verificationToken.getToken());
    }

    private VerificationToken getDefaultVerificationToken() {
        User user = User.builder().build();

        return VerificationToken.builder()
                .token(VERIFICATION_TOKEN)
                .user(user)
                .build();
    }
}