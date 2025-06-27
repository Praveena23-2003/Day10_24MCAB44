package org.example;

import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.util.Properties;

public class EmailUtil {
    public static void sendEmail(String toEmail, String subject, String body) {
        final String fromEmail = "24mcab44@kristujayanti.com"; // ✅ Your verified email
        final String appPassword = "ylds yeom yyqv eovm";         // ✅ 16-char App Password

        // SMTP server properties
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true"); // Enable debug for console logs

        // Auth session
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, appPassword);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            msg.setSubject(subject);
            msg.setText(body);

            System.out.println("📧 Sending email to " + toEmail);
            Transport.send(msg);
            System.out.println("✅ Email sent successfully!");
        } catch (MessagingException e) {
            System.out.println("❌ Failed to send email:");
            e.printStackTrace();
        }
    }
}
