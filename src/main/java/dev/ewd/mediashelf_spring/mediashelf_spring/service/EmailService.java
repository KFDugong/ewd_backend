package dev.ewd.mediashelf_spring.mediashelf_spring.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private final JavaMailSender javaMailSender;

    @Value("${email.sender}")
    private String senderEmail;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendVerificationEmail(String recipientEmail, String subjet, String verificationLink) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(recipientEmail);
        helper.setSubject(subjet);
        helper.setText(buildVerificationEmailContent(verificationLink), true);
        helper.setFrom(senderEmail);
        javaMailSender.send(message);
    }

    public void sendPasswordResetEmail(String recipientEmail, String resetPasswordUrl) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(recipientEmail);
        helper.setSubject("Password reset request");
        helper.setText("<p>Click the link below to reset your password:</p>" +
                "<a href=\"" + resetPasswordUrl + "\">Reset Password</a>" +
                "<p>Should the link above not work, please copy the following link into a new tab: " + resetPasswordUrl + ".</p>"+
                "<p>If you did not request a password reset, please ignore this email.</p>", true);
        helper.setFrom(senderEmail);
        javaMailSender.send(message);
    }

    private String buildVerificationEmailContent(String link) {
        return "<p>Hi,</p>" +
                "<p>Thank you for registering. Please click the link below to verify your email:</p>" +
                "<div style='margin: 20px 0;'>" +
                "    <a href=\"" + link + "\" style='background-color: #4CAF50; color: white; padding: 14px 25px; text-align: center; text-decoration: none; display: inline-block;'>" +
                "        Verify Your Email" +
                "    </a>" +
                "</div>" +
                "<p>If the button above doesn't work, copy and paste the following link in your browser:</p>" +
                "<p>" + link + "</p>" +
                "<p>This link will expire in 24 hours.</p>";
    }

    public void sendApprovalMessage(String email, String username) throws MessagingException{
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject("Admin Role Granted");
        helper.setText("Dear " + username + "\n\nYou have been granted Admin privileges. \n\nBest regards,\nMediaShelf Team");
        helper.setFrom(senderEmail);
        javaMailSender.send(message);
    }
}
