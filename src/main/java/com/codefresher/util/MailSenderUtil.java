package com.codefresher.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class MailSenderUtil  {
    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(String receiver, String subject, String message){
        MimeMessage mimeMailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage, "utf-8");
        try {
            helper.setFrom("PADSHOP");
            helper.setSubject(subject);
            helper.setText(message, true);
            helper.setTo(receiver);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        mailSender.send(mimeMailMessage);
    }
}
