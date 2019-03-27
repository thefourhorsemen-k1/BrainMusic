package com.horsemen.bmserver.controller;

import com.horsemen.bmserver.model.Replies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class ReplyController {
    @Autowired
    public JavaMailSender emailSender;

    @RequestMapping(value = "/admin/email")
    public void sendEmail(@RequestBody Replies reply) {
        sendSimpleMessage(reply.getEmail(), reply.getSubject(),
                reply.getFeedback());
    }

    public void sendSimpleMessage(
            String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
}
