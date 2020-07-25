package com.ccsu.community.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * 发送邮件工具类
 */
@Slf4j
@Component
public class EmailUtils {

    @Autowired
    JavaMailSender mailSender;
    @Value("${customize.sender}")
    String sender;

    public void sendEmail(String receiver,String title, String content) throws MailException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(title);
        message.setText(content);
        message.setTo(receiver);
        message.setFrom(sender);
        mailSender.send(message);
    }
}

