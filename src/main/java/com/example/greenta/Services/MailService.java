package com.example.greenta.Services;

import com.example.greenta.Models.User;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

public class MailService {
        Properties prop = new Properties();
        Session session;
    public MailService() {
            // SMTP Properties
            prop.put("mail.smtp.host", "sandbox.smtp.mailtrap.io");
            prop.put("mail.smtp.port", "2525");
            prop.put("mail.smtp.auth", "true");
            prop.put("mail.smtp.starttls.enable", "true");

            String email = "e464bfc862e6fb";
            String password = "c62f5098abe932";
        session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });
    }

    public void sendEmail(User to) throws MessagingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("no-reply@greenta.tn"));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(to.getEmail())
        );
        message.setSubject("Password reset request:" + to.getFirstname());

        String msg = "Dear " + to.getFirstname() + " " + to.getLastname() + ", your password has been updated successfully." +
                " If you didn't request this change, please contact us immediately. Thank you.";

        message.setText(msg);

        Transport.send(message);
    }
}
