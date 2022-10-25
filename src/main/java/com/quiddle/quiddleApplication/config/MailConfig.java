package com.quiddle.quiddleApplication.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${mailer.host}")
    private String host;

    @Value("${mailer.port}")
    private int port;

    @Value("${mailer.username}")
    private String username;

    @Value("${mailer.password}")
    private String password;

    @Value("${mailer.protocol}")
    private String protocol;

    @Value("${mailer.auth}")
    private boolean auth;

    @Value("${mailer.starttls}")
    private boolean starttls;

    @Value("${mailer.debug}")
    private boolean debug;

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost(host);
        mailSender.setPort(port);

        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();

        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.auth", auth);
        props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.debug", debug);

        return mailSender;
    }
}
