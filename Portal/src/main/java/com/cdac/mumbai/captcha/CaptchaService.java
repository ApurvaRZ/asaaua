package com.cdac.mumbai.captcha;

import java.awt.image.BufferedImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.code.kaptcha.impl.DefaultKaptcha;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class CaptchaService {

    @Resource(name = "captchaProducer")
    @Autowired
    private DefaultKaptcha captchaGenerator;

    /**
     * Generates a CAPTCHA image based on the provided text.
     *
     * @param captchaText the text to include in the CAPTCHA image
     * @return the generated CAPTCHA image
     */
    public BufferedImage generateCaptchaImage(String captchaText) {
        log.info("Generating CAPTCHA image for text: {}", captchaText);
        return captchaGenerator.createImage(captchaText);
    }

    /**
     * Generates a random CAPTCHA text of the given length.
     *
     * @param length the length of the CAPTCHA text
     * @return the randomly generated CAPTCHA text
     */
    public String generateRandomCaptchaText(int length) {
        StringBuilder captchaText = new StringBuilder();
        for (int i = 0; i < length; i++) {
            captchaText.append((char) ('A' + Math.random() * ('Z' - 'A' + 1)));
        }
        String generatedText = captchaText.toString();
        log.debug("Generated CAPTCHA text: {}", generatedText);
        return generatedText;
    }

    /**
     * Saves the CAPTCHA text in the current HTTP session.
     *
     * @param captchaText the CAPTCHA text to save
     * @param session     the current HTTP session
     */
    public void saveCaptchaTextInSession(String captchaText, HttpSession session) {
        log.info("Saving CAPTCHA text to session");
        session.setAttribute("captchaText", captchaText);
    }

    /**
     * Retrieves the CAPTCHA text from the current HTTP session.
     *
     * @param session the current HTTP session
     * @return the CAPTCHA text stored in the session, or null if not present
     */
    public String getCaptchaTextFromSession(HttpSession session) {
        String captchaText = (String) session.getAttribute("captchaText");
        log.debug("Retrieved CAPTCHA text from session: {}", captchaText);
        return captchaText;
    }
}
