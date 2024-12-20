package com.cdac.mumbai.captcha;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/captcha")
@CrossOrigin(origins = "*")
@Slf4j
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;

    @GetMapping("/generate")
    public ResponseEntity<String> generateCaptcha(HttpSession session) {
        try {
            log.info("Generating CAPTCHA");
            String captchaText = captchaService.generateRandomCaptchaText(6);
            log.debug("Generated CAPTCHA text: {}", captchaText);

            BufferedImage captchaImage = captchaService.generateCaptchaImage(captchaText);
            if (captchaImage == null) {
                log.error("Failed to generate CAPTCHA image");
                return ResponseEntity.internalServerError().build();
            }

            byte[] imageBytes = imageToBytes(captchaImage);
            log.debug("CAPTCHA image converted to byte array, size: {}", imageBytes.length);

            // Save the original CAPTCHA text in the session
            captchaService.saveCaptchaTextInSession(captchaText, session);
            log.info("CAPTCHA text saved in session");

            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            log.debug("CAPTCHA image encoded to Base64");
            return ResponseEntity.ok(base64Image);
        } catch (Exception e) {
            log.error("Error generating CAPTCHA", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validateCaptcha(@RequestParam String userInput, HttpSession session) {
        try {
            log.info("Validating CAPTCHA");
            // Retrieve the original CAPTCHA text from the session
            String captchaText = captchaService.getCaptchaTextFromSession(session);
            log.debug("Retrieved CAPTCHA text from session: {}", captchaText);

            // Compare the user's input with the original CAPTCHA text
            if (captchaText.equalsIgnoreCase(userInput)) {
                log.info("CAPTCHA validation successful");
                return ResponseEntity.ok("Captcha validation successful");
            } else {
                log.warn("CAPTCHA validation failed. User input: {}, Expected: {}", userInput, captchaText);
                return ResponseEntity.badRequest().body("Captcha validation failed");
            }
        } catch (Exception e) {
            log.error("Error validating CAPTCHA", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    private byte[] imageToBytes(BufferedImage image) throws Exception {
        try {
            log.debug("Converting BufferedImage to byte array");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, "png", baos);
            log.debug("Image successfully written to byte array");
            return baos.toByteArray();
        } catch (Exception e) {
            log.error("Error converting image to byte array", e);
            throw e;
        }
    }
}
