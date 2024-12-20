package com.cdac.mumbai.common;

import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.cdac.mumbai.model.Email;
import com.cdac.mumbai.model.User;
import com.cdac.mumbai.service.EmailService;
import com.cdac.mumbai.service.UserService;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@PropertySource("classpath:messages_mail.properties")
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private UserService service;

    @Autowired
    private MessageSource messages;

    @Autowired
    private Environment env;

    @Autowired
    private EmailService emailService;

    @PostConstruct
    public void init() {
        if (messages == null) {
            log.error("MessageSource bean is not injected");
            throw new IllegalStateException("MessageSource bean is not injected");
        }
        log.info("MessageSource bean injected successfully");
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        try {
            this.confirmRegistration(event);
        } catch (Exception e) {
            log.error("Error during registration confirmation: {}", e.getMessage(), e);
        }
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) throws Exception {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();

        try {
            service.createVerificationTokenForUser(user, token);
            log.info("Verification token created for user: {}", user.getCd_name());

            String recipientAddress = user.getCdemail();
            String encryptedToken = encryptToken(token, event);

            String emailContent = buildEmailContent(user, encryptedToken, event);

            try {
                sendConfirmationEmail(recipientAddress, emailContent);
                log.info("Confirmation email sent to: {}", recipientAddress);
            } catch (Exception e) {
                log.error("Failed to send confirmation email to: {}", recipientAddress, e);
                throw new Exception("Failed to send confirmation email, but user registration is complete.");
            }
        } catch (Exception e) {
            log.error("Error in Registration Listener: {}", e.getMessage(), e);
            throw new Exception("Registration failed at confirmation email step.");
        }
    }

    private String encryptToken(String token, OnRegistrationCompleteEvent event) throws Exception {
        String queryString = "token=" + token;
        log.debug("Query String before encryption: {}", queryString);

        // Encrypt the token with the public cipher key
        return new RSACipher().encrypt(queryString);
    }

    private String buildEmailContent(User user, String encryptedToken, OnRegistrationCompleteEvent event) {
        String message = messages.getMessage("message.mailVerifiednew", null, null);
       // String mailSignature = messages.getMessage("message.thanksmailmsg", null, null);
        String userMessage = messages.getMessage("message.usermailmsg", null, null);
        String linkValidity = messages.getMessage("message.linkvalidity", null, null);

        String confirmationUrl = event.getAppUrl() + "/user/registrationConfirm?est=" + encryptedToken;

        return userMessage + " " + user.getCd_name() + ",<br><br><b><a href=\"" + confirmationUrl
                + "\">Click Here</a></b> " + message + linkValidity + "<br><br>" ;
    }

    private void sendConfirmationEmail(String recipientAddress, String emailContent) throws Exception {
        Email email = new Email();
        email.setTo(recipientAddress);
        email.setMailcontent(emailContent);
        email.setSignature(messages.getMessage("message.thanksmailmsg", null, null));
        email.setSubject(messages.getMessage("message.sub.emailverify", null, null));

        emailService.sendEmail(email, env.getProperty("smtp.host"), env.getProperty("smtp.port"),
                env.getProperty("smtp.user"), env.getProperty("smtp.pass"), recipientAddress);
    }
}
