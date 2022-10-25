package com.quiddle.quiddleApplication.listeners;

import com.quiddle.quiddleApplication.dto.VerificationDto;
import com.quiddle.quiddleApplication.events.OnRegistrationCompleteEvent;
import com.quiddle.quiddleApplication.models.User;
import com.quiddle.quiddleApplication.services.VerificationTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
public class RegistrationListener implements
        ApplicationListener<OnRegistrationCompleteEvent> {

    @Value("${mailer.from}")
    private String from;

    private final VerificationTokenService verificationTokenService;

    private final JavaMailSender mailSender;

    public RegistrationListener(VerificationTokenService verificationTokenService, JavaMailSender mailSender) {
        this.verificationTokenService = verificationTokenService;
        this.mailSender = mailSender;
    }

    @Async
    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.startUserValidationProcess(event);
    }

    private void startUserValidationProcess(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();

        verificationTokenService.createVerificationToken(VerificationDto.getValidationToken(token, user));

        SimpleMailMessage email = buildVerificationEmail(user, event.getAppUrl(), token);

        mailSender.send(email);
    }

    private SimpleMailMessage buildVerificationEmail(User user, String appUrl, String token) {
        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = "/auth/verify?token=" + token;
        String body = String.format("Welcome to School SAAS \r\n\nKindly navigate to the link below to verify your account \r\n\n%s%s", appUrl, confirmationUrl);

        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(from);
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(body);

        return email;
    }
}
