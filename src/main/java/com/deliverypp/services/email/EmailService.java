package com.deliverypp.services.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service("emailService")
public class EmailService {

    @Autowired
    private JavaMailSender sender;

    //@Autowired
    //private SimpleMailMessage preConfiguredMessage;


    /**
     * This method will send compose and send the message
     * */
    public void sendMail() {

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {

            helper.setTo("appstive@outlook.com");
            helper.setText("Testing body");
            helper.setSubject("Testing");

        } catch(MessagingException e) {
            System.out.println(e.getMessage());
        }

        sender.send(message);

    }

}
