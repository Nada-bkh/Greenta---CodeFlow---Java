package Services;

import Models.User;
import jakarta.activation.DataHandler;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.mail.util.ByteArrayDataSource;
import java.net.Authenticator;
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

            String email = "83defc1d88b91f";
            String password = "8ba83e15a1a798";
            session = Session.getInstance(prop, new jakarta.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(email, password);
                }
            });
        }

        public void sendEmail (User to) throws MessagingException {
            Properties prop = new Properties();
            prop.put("mail.smtp.auth", false);
            prop.put("mail.smtp.starttls.enable", "true");
            prop.put("mail.smtp.host", "smtp.freesmtpservers.com");
            prop.put("mail.smtp.port", "25");

            Session session = Session.getInstance(prop, new jakarta.mail.Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("", "");
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("no-reply@greenta.com"));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(to.getEmail()));
            message.setSubject("Password reset request:" + to.getFirstname());

            String msg = "Dear " + to.getFirstname() + " " + to.getLastname() + "your password has been updated successfully." +
                    " If you didn't request this change please contact us. Thank you.";

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(msg, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            Transport.send(message);

        }
}
