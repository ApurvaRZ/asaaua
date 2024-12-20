package com.cdac.mumbai.service.impl;

import java.io.IOException;
import java.util.Properties;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cdac.mumbai.model.Email;
import com.cdac.mumbai.service.EmailService;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Override
    public boolean sendEmail(Email email, String smtphost, String smtpport, String smtpuser, String smtppass, String emailids) {
        log.info("Starting to send email. To: {}, Subject: {}", emailids, email.getSubject());

        // Validate email input
        if (email == null || email.getTo() == null || email.getSubject() == null) {
            log.error("Invalid email input: {}", email);
            return false;
        }

        // Construct email message content
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(email.getMailcontent().replaceAll("\n", "<br>"));
        messageBuilder.append(email.getSignature().replaceAll("\n", "<br>"));
        String messageContent = messageBuilder.toString();

        // Set up email properties
        Properties props = new Properties();
        props.put("mail.smtp.transport.protocol", "smtp");
        props.put("mail.smtp.host", smtphost);
        props.put("mail.smtp.port", smtpport);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.debug", "false");
        props.put("mail.smtp.localhost", "127.0.0.1");

        final String userId = smtpuser;
        final String userPassword = smtppass;
        Session session = Session.getDefaultInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userId, userPassword);
            }
        });

        try {
            // Create email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(smtpuser));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailids));
            message.setSubject(email.getSubject());

            // Create message body part with HTML content
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(messageContent, "text/html");

            // Create multipart message for content and attachments
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            // Handle attachments if any
            if (email.getAttach() != null) {
                log.info("Processing attachments.");
                for (MultipartFile file : email.getAttach()) {
                    if (!file.isEmpty()) {
                        log.debug("Adding attachment: {}", file.getOriginalFilename());

                        // Create body part for attachment
                        messageBodyPart = new MimeBodyPart();
                        DataSource source = new ByteArrayDataSource(file.getBytes(), file.getContentType());
                        messageBodyPart.setDataHandler(new DataHandler(source));
                        messageBodyPart.setFileName(file.getOriginalFilename());
                        multipart.addBodyPart(messageBodyPart);
                    } else {
                        log.warn("Empty file detected, skipping attachment.");
                    }
                }
            }

            // Set the complete email content
            message.setContent(multipart);

            // Send email using transport
            log.info("Sending email through SMTP transport.");
            Transport.send(message);
            log.info("Email sent successfully to: {}", emailids);
            return true;
        } catch (MessagingException | IOException e) {
            log.error("Failed to send email. Exception: {}", e.getMessage(), e);
            return false;
        }
    }
}
