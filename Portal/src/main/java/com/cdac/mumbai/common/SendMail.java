package com.cdac.mumbai.common;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.cdac.mumbai.dto.UserUpdation;
import com.cdac.mumbai.model.Email;
import com.cdac.mumbai.model.User;
import com.cdac.mumbai.model.VerificationToken;
import com.cdac.mumbai.service.EmailService;
import com.cdac.mumbai.service.SaConfigParaService;
import com.cdac.mumbai.service.impl.UserServiceImpl;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@PropertySource("classpath:messages_mail.properties")
public class SendMail {

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private SaConfigParaService sa;

    @Autowired
    private MessageSource messages;

    @Autowired
    private Environment env;
    
    @Autowired
	  PasswordEncoder p;

    @PostConstruct
    public void init() {
        if (messages == null) {
            log.error("MessageSource bean is not injected");
            throw new IllegalStateException("MessageSource bean is not injected");
        } else {
            log.info("MessageSource bean injected successfully");
        }
    }
    public String resetPassword(User usr) {
        log.info("Starting resetPassword process for user: {}", usr.getCdemail());

        String message = messages.getMessage("message.generatePassword", null, null);
        String mail = messages.getMessage("message.email", null, null);
        String textmail = messages.getMessage("message.thanksmailmsg", null, null);
        String textuser = messages.getMessage("message.usermailmsg", null, null);
        
        try {
            log.info("Preparing email content for user: {}", usr.getCdemail());

            Email htmlmail = new Email();
            String mailMessage = textuser + " " + usr.getCd_name() + ",\n\n" + message;

            htmlmail.setTo(usr.getCdemail());
            htmlmail.setMailcontent(mailMessage);
            htmlmail.setSignature(textmail);
            htmlmail.setSubject(messages.getMessage("subpassupdate", null, null));

            log.info("Attempting to send email to: {}", usr.getCdemail());
            boolean flag = emailService.sendEmail(htmlmail, env.getProperty("smtp.host"), env.getProperty("smtp.port"),
                    env.getProperty("smtp.user"), env.getProperty("smtp.pass"), usr.getCdemail());

            if (!flag) {
                log.error("Failed to send reset password email to user: {}", usr.getCdemail());
                return "failtosendmail";
            }

        } catch (Exception e) {
            log.error("Exception occurred while sending reset password email to user: {}", usr.getCdemail(), e);
            return "failtosendmail";
        }

        log.info("Successfully sent reset password email to user: {}", usr.getCdemail());
        return "successtosendmail";
    }

    public String updatePassEmail(User usr) {
        log.info("Starting updatePassEmail for user: {}", usr.getCdemail());
        String message = messages.getMessage("message.generatePassword", null, null);
        String mail = messages.getMessage("message.email", null, null);
        String textmail = messages.getMessage("message.thanksmailmsg", null, null);
        String textuser = messages.getMessage("message.usermailmsg", null, null);
        
        try {
            log.debug("Preparing email content for user: {}", usr.getCdemail());
            
            Email htmlmail = new Email();
            String mailMessage = textuser + " " + usr.getCdemail() + ",\n\n" + message;

            htmlmail.setTo(usr.getCdemail());
            htmlmail.setMailcontent(mailMessage);
            htmlmail.setSignature(textmail);
            htmlmail.setSubject(messages.getMessage("subpassupdate", null, null));

            log.debug("Sending email to: {}", usr.getCdemail());
            boolean flag = emailService.sendEmail(htmlmail, env.getProperty("smtp.host"), env.getProperty("smtp.port"),
                    env.getProperty("smtp.user"), env.getProperty("smtp.pass"), usr.getCdemail());

            if (!flag) {
                log.error("Failed to send email to user: {}", usr.getCdemail());
                return "failtosendmail";
            }

        } catch (Exception e) {
            log.error("Exception occurred while sending email to user: {}", usr.getCdemail(), e);
            return "failtosendmail";
        }

        log.info("Successfully sent password update email to user: {}", usr.getCdemail());
        return "successtosendmail";
    }

    
    
    
    
    public String confirmationEmail(VerificationToken verification) {
        String recipientAddress = verification.getUser().getCdemail();
        String adminEmail = messages.getMessage("message.email", null, Locale.getDefault()); // Assuming this is the admin email
        boolean userMailSent = false;
        boolean adminMailSent = false;

        try {
            log.info("Preparing to send confirmation email to user: {}", recipientAddress);

            // Fetch messages for the email
            String message = messages.getMessage("message.confirmationEmailmsg", null, Locale.getDefault());
            String message2 = messages.getMessage("message.confirmationEmail2", null, Locale.getDefault());
            String text = messages.getMessage("message.admin", null, Locale.getDefault());
            String textmail = messages.getMessage("message.thanksmailmsg", null, Locale.getDefault());
            String textuser = messages.getMessage("message.usermailmsg", null, Locale.getDefault());
            String subject = messages.getMessage("registrationsucesssubject", null, Locale.getDefault());

            // Construct the email content for the user
            String mailMessage = textuser + " " + verification.getUser().getCd_name() + ", " + message
                    + " Reg ID: " + verification.getUser().getRegId() + ", Username: " + verification.getUser().getUsername()
                    + ", Password: " + verification.getUser().getPassword() + ".\n" + message2;

            // Prepare email object for the user
            Email userMail = new Email();
            userMail.setTo(recipientAddress);
            userMail.setMailcontent(mailMessage);
            userMail.setSignature(textmail);
            userMail.setSubject(subject);

            // Send email to the user
            userMailSent = emailService.sendEmail(userMail, env.getProperty("smtp.host"), env.getProperty("smtp.port"),
                    env.getProperty("smtp.user"), env.getProperty("smtp.pass"), recipientAddress);

            if (!userMailSent) {
                log.error("Failed to send confirmation email to user: {}", recipientAddress);
                return "Failed to send confirmation email to user.";
            }

            log.info("Confirmation email successfully sent to user: {}", recipientAddress);

            // Construct the email content for the admin
            String adminMailMessage = text + subject + ".\nOrganization Name: " + verification.getUser().getOrganizationName()
                    + "\nReg ID: " + verification.getUser().getRegId() + ".";

            // Prepare email object for the admin
            Email adminMail = new Email();
            adminMail.setTo(adminEmail);
            adminMail.setMailcontent(adminMailMessage);
            adminMail.setSignature(textmail);
            adminMail.setSubject(subject);

            // Send email to the admin
            adminMailSent = emailService.sendEmail(adminMail, env.getProperty("smtp.host"), env.getProperty("smtp.port"),
                    env.getProperty("smtp.user"), env.getProperty("smtp.pass"), adminEmail);

            if (!adminMailSent) {
                log.error("Failed to send confirmation email to admin: {}", adminEmail);
                return "Confirmation email sent to user but failed to send to admin.";
            }

            log.info("Confirmation email successfully sent to admin: {}", adminEmail);

        } catch (Exception e) {
            log.error("Error occurred during confirmation email sending: {}", e.getMessage(), e);
            return "Error occurred while sending emails.";
        }

        // If both emails are successfully sent, return a success message
        return "Confirmation emails successfully sent to both user and admin.";
    }


    
    public String resendTokenEmail(VerificationToken verification, HttpServletRequest request) {
        String recipientAddress = verification.getUser().getCdemail();
        log.info("Resending token email to: {}", recipientAddress);

        try {
            String querystring = "token=" + verification.getToken();
            log.debug("Generated query string: {}", querystring);

            String mailMessage = "Dear " + verification.getUser().getCd_name() + ",\n\n" +
                    messages.getMessage("message.mailVerifiednew", null, null);

            Email htmlmail = new Email();
            htmlmail.setTo(recipientAddress);
            htmlmail.setMailcontent(mailMessage);
            htmlmail.setSignature(messages.getMessage("message.thanksmailmsg", null, null));
            htmlmail.setSubject(messages.getMessage("subresendnew", null, null));

            boolean flag = emailService.sendEmail(htmlmail, env.getProperty("smtp.host"), env.getProperty("smtp.port"),
                    env.getProperty("smtp.user"), env.getProperty("smtp.pass"), recipientAddress);

            if (!flag) {
                log.error("Failed to resend token email to {}", recipientAddress);
                return "failtosendmail";
            }

            log.info("Resend token email successfully sent to {}", recipientAddress);
        } catch (Exception e) {
            log.error("Error while resending token email to {}: {}", recipientAddress, e.getMessage(), e);
            return "failtosendmail";
        }
        return "successtosendmail";
    }

    public String resendTokenEmail4Resetpass(VerificationToken vt, HttpServletRequest request) {
        String recipientAddress = vt.getUser().getCdemail();

        // Log the recipient email for debugging purposes
        log.info("Resending reset password email to: {}", recipientAddress);

        // Fetching localized messages
        log.info("Fetching localized message strings.");
        String message = messages.getMessage("message.resetPasswordNew", null, null);
        String textmail = messages.getMessage("message.thanksmailmsg", null, null);
        String textuser = messages.getMessage("message.usermailmsg", null, null);
        String linkvalid = messages.getMessage("message.linkvalidity", null, null);

        try {
            // Log the public key being retrieved
            log.info("Retrieving email encryption public key.");
         //   String publicKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAK6r9oOLqaUEzIA0W9LNnmPtP2YePfWNtiU32dKHCNdQrGyl2mx6xUcxUWD7dWwPAqLfwZVVqK8jwgPk6KkdJKkCAwEAAQ==";//sa.getSaConfigKeys("email_encrypt_public_key");
           // log.info("Public key retrieved: {}", publicKey);
            String privateKey = sa.getSaConfigKeys("email_encrypt_private_key");
            String encrypted = new RSACipher().encrypt("Apurva");
            String deceypt = new RSACipher().decrypt(encrypted);
            
            log.info(" New Text : {} ",deceypt);
            // Create the query string
            log.info("Creating query string for reset token.");
            String querystring = "id=" + vt.getUser().getRegId() + "&token=" + vt.getToken();
            log.info("Generated query string: {}", querystring);

            // Encrypt the token
            log.info("Encrypting the query string.");
            String encryptedToken = new RSACipher().encrypt(querystring);
            log.info("Encrypted token: {}", encryptedToken);

            // Generate the reset URL
            log.info("Generating the reset URL.");
            String resetUrl = new GeneralMethods().getAppUrl(request) + "/user/changePassword?est=" + encryptedToken;
            log.info("Reset URL: {}", resetUrl);

            // Create the email content
            log.info("Constructing email content.");
            String mailMessage = textuser + " " + vt.getUser().getCd_name() + ",<br><br>" 
                    + message + "<b><a href=\"" + resetUrl + "\">Click Here</a></b><br>" 
                    + linkvalid;
            log.info("Email content created.");

            // Prepare email object
            log.info("Preparing email object.");
            Email htmlmail = new Email();
            htmlmail.setTo(recipientAddress);
            htmlmail.setMailcontent(mailMessage);
            htmlmail.setSignature(textmail);
            htmlmail.setSubject(messages.getMessage("subreset", null, null));
            log.debug("Email object: {}", htmlmail);

            // Send the email
            log.info("Sending the email.");
            boolean flag = emailService.sendEmail(htmlmail, 
                                                  env.getProperty("smtp.host"), 
                                                  env.getProperty("smtp.port"),
                                                  env.getProperty("smtp.user"), 
                                                  env.getProperty("smtp.pass"), 
                                                  recipientAddress);
            log.info("Email send status: {}", flag);

            // Check if the email was sent successfully
            if (!flag) {
                log.error("Failed to send reset password email to {}", recipientAddress);
                return "failtosendmail";
            }

            log.info("Reset password email successfully sent to {}", recipientAddress);

        } catch (Exception e) {
            log.error("Error while sending reset password email to {}: {}", recipientAddress, e.getMessage(), e);
            return "failtosendmail";
        }
        return "successtosendmail";
    }

    
    public String updateProfileEmail(UserUpdation up) {
        String recipientAddress = up.getCd_email();
        log.info("Sending profile update email to: {}", recipientAddress);

        try {
            String mailMessage = "Dear " + up.getCd_name() + ",\n\n" + messages.getMessage("message.updateProfileToUser", null, null);

            Email htmlmail = new Email();
            htmlmail.setTo(recipientAddress);
            htmlmail.setMailcontent(mailMessage);
            htmlmail.setSignature(messages.getMessage("message.thanksmailmsg", null, null));
            htmlmail.setSubject(messages.getMessage("profileupdatesubject", null, null));

            boolean flag = emailService.sendEmail(htmlmail, env.getProperty("smtp.host"), env.getProperty("smtp.port"),
                    env.getProperty("smtp.user"), env.getProperty("smtp.pass"), recipientAddress);

            if (!flag) {
                log.error("Failed to send profile update email to {}", recipientAddress);
                return "failtosendmail";
            }

            log.info("Profile update email successfully sent to {}", recipientAddress);
        } catch (Exception e) {
            log.error("Error while sending profile update email to {}: {}", recipientAddress, e.getMessage(), e);
            return "failtosendmail";
        }
        return "successtosendmail";
    }

    public String whitelistingMailToAdmin(User user, String IP) {
        log.info("Sending IP whitelisting request email to admin");

        try {
            String mailMessage = "Admin,\n\nUsername: " + user.getUsername() + " has requested IP whitelisting.\nIP Address: " + IP;

            Email htmlmail = new Email();
            htmlmail.setTo(messages.getMessage("message.email", null, null));
            htmlmail.setMailcontent(mailMessage);
            htmlmail.setSignature(messages.getMessage("message.thanksmailmsg", null, null));
            htmlmail.setSubject(messages.getMessage("whitelistingsubject", null, null));

            boolean flag = emailService.sendEmail(htmlmail, env.getProperty("smtp.host"), env.getProperty("smtp.port"),
                    env.getProperty("smtp.user"), env.getProperty("smtp.pass"), htmlmail.getTo());

            if (!flag) {
                log.error("Failed to send IP whitelisting email for user {}", user.getUsername());
                return "failtosendmail";
            }

            log.info("IP whitelisting request email sent to admin for user {}", user.getUsername());
        } catch (Exception e) {
            log.error("Error while sending IP whitelisting email for user {}: {}", user.getUsername(), e.getMessage(), e);
            return "failtosendmail";
        }
        return "successtosendmail";
    }

    public String deleteWhitelistedIpMailToAdmin(User user, String IP) {
        log.info("Sending delete whitelisted IP request email to admin");

        try {
            String mailMessage = "Admin,\n\nUsername: " + user.getUsername() + " has requested deletion of whitelisted IP.\nIP Address: " + IP;

            Email htmlmail = new Email();
            htmlmail.setTo(messages.getMessage("message.email", null, null));
            htmlmail.setMailcontent(mailMessage);
            htmlmail.setSignature(messages.getMessage("message.thanksmailmsg", null, null));
            htmlmail.setSubject(messages.getMessage("deleteipgsubject", null, null));

            boolean flag = emailService.sendEmail(htmlmail, env.getProperty("smtp.host"), env.getProperty("smtp.port"),
                    env.getProperty("smtp.user"), env.getProperty("smtp.pass"), htmlmail.getTo());

            if (!flag) {
                log.error("Failed to send delete whitelisted IP email for user {}", user.getUsername());
                return "failtosendmail";
            }

            log.info("Delete whitelisted IP email sent to admin for user {}", user.getUsername());
        } catch (Exception e) {
            log.error("Error while sending delete whitelisted IP email for user {}: {}", user.getUsername(), e.getMessage(), e);
            return "failtosendmail";
        }
        return "successtosendmail";
    }
}
