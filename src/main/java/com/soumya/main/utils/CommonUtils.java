package com.soumya.main.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CommonUtils {

    @Autowired
    private JavaMailSender javaMailSender;

    public boolean sendResetEmail(String recipientEmail, String url){
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("choudhurysoumya87@gmail.com");
            simpleMailMessage.setTo(recipientEmail);
            simpleMailMessage.setSubject("Password Reset Link Finance Tracker App");

            String content = "Hello, You have requested to reset your password"
                    + "Click the link below to change your password:" + "<a href=\"" + url
                    + "\">Change my password</a>";

            simpleMailMessage.setText(content);
            javaMailSender.send(simpleMailMessage);
            return  true;
        }catch (Exception exception){
            exception.printStackTrace();
            return true;
        }
    }

    public static String generateResetToken(){
        return UUID.randomUUID().toString();
    }

    public static String generateUrl(HttpServletRequest request) {

        String siteUrl = request.getRequestURL().toString(); //http://localhost:1010/passwordResetField

        //before Servlet Path= /passwordRestFiled

        String url = siteUrl.replace(request.getServletPath(), "");

        return url;
    }
}

