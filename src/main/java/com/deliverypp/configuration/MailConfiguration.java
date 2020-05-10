package com.deliverypp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfiguration {

    @Bean
    public JavaMailSenderImpl mailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(25);
        mailSender.setUsername("iletknowapp@gmail.com");
        mailSender.setPassword("zstegexqsgmjawup");
        return mailSender;

    }
}
