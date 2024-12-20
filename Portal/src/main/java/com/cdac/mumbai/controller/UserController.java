package com.cdac.mumbai.controller;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cdac.mumbai.dto.AuthenticationRequest;
import com.cdac.mumbai.dto.AuthenticationResponse;
import com.cdac.mumbai.dto.UserRegistration;
import com.cdac.mumbai.dto.UserUpdation;
import com.cdac.mumbai.exception.ApiRequestException;
import com.cdac.mumbai.exception.ErrorResponse;
import com.cdac.mumbai.jwt.UserPrincipal;
import com.cdac.mumbai.model.Mstate;
import com.cdac.mumbai.model.User;
import com.cdac.mumbai.service.StateService;
import com.cdac.mumbai.service.StatusService;
import com.cdac.mumbai.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/user")
//@CrossOrigin(origins = "*")
@Slf4j
@PropertySource("classpath:messages_mail.properties")
public class UserController {
	
	@Autowired
	UserService us;
	
	@Autowired
	StateService st;
	
	@Autowired
	StatusService sr;
	
	@PostMapping("/register")
	public ResponseEntity<String> register(@RequestBody UserRegistration user, HttpServletRequest request) {
	    log.info("Received registration request for email: {}", user.getCdemail());

	    if (us.userExists(user.getCdemail())) {
	    	//log.warn("Inside if");
	        String message = "User with email " + user.getCdemail() + " already exists";
	        log.warn(message);
	        return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
	    }
        String registeredUser = us.register(user, request);
	    log.info("User registration successful for email: {}", user.getCdemail());

	    return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
	}

	 
	@GetMapping("/registrationConfirm")
	public ResponseEntity<String> confirmRegistration(Locale locale, @RequestParam("est") String est) {
	    log.info("Received registration confirmation request with token: {}", est);

	    // Call the validateVerificationToken method and capture the result
	    String result = us.validateVerificationToken(est);

	    // Handle various cases based on the result of the validation
	    switch (result) {
	        case "TOKEN_INVALID":
	            log.warn("Registration confirmation failed: Invalid token.");
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token.");

	        case "TOKEN_EXPIRED":
	            log.warn("Registration confirmation failed: Token expired.");
	            return ResponseEntity.status(HttpStatus.GONE).body("The token has expired.");

	        case "Failed to send confirmation email to the user.":
	            log.error("Failed to send confirmation email to the user.");
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send confirmation email to the user.");

	        case "Confirmation email sent to the user, but failed to send to the admin.":
	            log.error("Confirmation email sent to user, but failed to send to admin.");
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Confirmation email sent to the user, but failed to send to the admin.");

	        case "Error occurred while sending emails.":
	            log.error("Error occurred while sending emails.");
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while sending emails.");

	        case "Emails sent successfully, verification completed.":
	            log.info("Registration confirmed successfully.");
	            return ResponseEntity.status(HttpStatus.CREATED).body("Registration confirmed successfully, emails sent.");

	        default:
	            // Unexpected error or any other case
	            log.error("Unexpected error occurred during registration confirmation.");
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occurred.");
	    }
	}

	
	@GetMapping("/resendRegistrationToken")
	public ResponseEntity<String> resendRegistrationToken(HttpServletRequest request, @RequestParam("token") String existingToken) {
	    log.info("Received request to resend registration token for token: {}", existingToken);
	    
	    String result = us.generateNewVerificationToken(existingToken, request);
	    
	    if (result != null) {
	        log.debug("New registration token generated for token: {}", existingToken);
	        return ResponseEntity.status(HttpStatus.CREATED).body(result);
	    }
	    
	    log.warn("Failed to resend registration token for token: {}", existingToken);
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	
	@PostMapping("/resetPassword")
	public ResponseEntity<String> resetPassword(HttpServletRequest request, @RequestParam("email") String userEmail) {
	    log.info("Received reset password request for email: {}", userEmail);
	    
	    String result = us.resetPassword(userEmail, request);
	    
	    if (result != null) {
	        log.debug("Reset password email sent to: {}", userEmail);
	        return ResponseEntity.status(HttpStatus.CREATED).body(result);
	    }
	    
	    log.warn("Failed to reset password for email: {}", userEmail);
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	 
	@GetMapping("/changePassword")
	public ResponseEntity<String> changePassword(@RequestParam("est") String est) {
	    log.info("Received change password request with token: {}", est);
	    
	    String result = us.validateResetToken(est);
	    
	    
	    
	    if (result.contains("@")) {
	        log.debug("Password reset token validated: {}", est);
	        return ResponseEntity.status(HttpStatus.CREATED).body(result);
	    }
	    
	    log.warn("Password reset token validation failed: {}", est);
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Return to login page");
	}
	 
	@PostMapping("/savePassword")
	public ResponseEntity<String> savePassword(@RequestParam("password") String password, @RequestParam("email") String userEmail) {
	    log.info("Received request to save new password for user: {}", userEmail);
	    
	    String result = us.updateDefaultPassword(userEmail, password);
	    
	    if (result == "successtosendmail") {
	        log.debug("Password updated successfully for user: {}", userEmail);
	        return ResponseEntity.status(HttpStatus.CREATED).body("Show login page");
	    }
	    
	    log.warn("Failed to update password for user: {}", userEmail);
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to update default password");
	}
	 
	@PostMapping("/updatePassword")
	@Operation(security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<String> updatePassword(@AuthenticationPrincipal UserPrincipal user, @RequestParam("password") String password, @RequestParam("oldpassword") String oldPassword) {
	    log.info("Received request to update password for user: {}", user.getUsername());
	    
	    String result = us.changePassword(password, oldPassword, user.getUsername());
	    
	    if (result != null) {
	        log.debug("Password updated successfully for user: {}", user.getUsername());
	        return ResponseEntity.status(HttpStatus.CREATED).body(result);
	    }
	    
	    log.warn("Failed to update password for user: {}", user.getUsername());
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Return to login page");
	}
	 
	@GetMapping("/deptCount")
	public ResponseEntity<Integer> deptCount() {
	    log.info("Received request to get department count.");
	    
	    Integer result = us.getDeptCount();
	    
	    if (result != null) {
	        log.debug("Department count retrieved: {}", result);
	        return ResponseEntity.status(HttpStatus.CREATED).body(result);
	    }
	    
	    log.warn("Failed to retrieve department count.");
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	 
	@GetMapping("/states")
	public ResponseEntity<List<Mstate>> getMessages() {
	    log.info("Received request to get list of states.");
	    
	    List<Mstate> states = st.getState();
	    log.debug("States list retrieved successfully.");
	    
	    return new ResponseEntity<List<Mstate>>(states, HttpStatus.OK);
	}
	 
	
	@GetMapping("/info")
	public ResponseEntity<User> userInfo(@AuthenticationPrincipal UserPrincipal user){
		 log.info("Received request to update user information for user: {}", user.getUsername());
		return ResponseEntity.ok(us.getDetails(user.getUsername()));
	}

	@PutMapping("/update")
	@Operation(security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<String> updateUser(@AuthenticationPrincipal UserPrincipal user, @Valid @RequestBody UserUpdation userUpdation) {
	    log.info("Received request to update user information for user: {}", user.getUsername());
	    
	    String result = us.updateUserInformation(user.getUsername(), userUpdation);
	    
	    if (result != null) {
	        log.debug("User information updated successfully for user: {}", user.getUsername());
	        return ResponseEntity.status(HttpStatus.CREATED).body(result);
	    }
	    
	    log.warn("Failed to update user information for user: {}", user.getUsername());
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}
	 
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody AuthenticationRequest request, HttpServletResponse response) {
	    log.info("Login request received for email: {}", request.getUsername());

	    try {
	        AuthenticationResponse authResponse = us.login(request, response);
	        log.debug("Login successful for email: {}", request.getUsername());
	        return ResponseEntity.ok(authResponse);
	    } catch (ApiRequestException ex) {
	        log.error("Login failed for email: {}: {}", request.getUsername(), ex.getMessage(), ex);
	        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
	        return ResponseEntity.status(ex.getStatus())
                    .body(Map.of("message", ex.getMessage()));
	    }
	}
	 @PostMapping("/refresh")
	 @Operation(security = @SecurityRequirement(name = "bearerAuth"))
	    public ResponseEntity<?> refreshToken(@CookieValue("refresh_token") String refreshToken, HttpServletResponse response) {
	        try {
	            AuthenticationResponse authResponse = us.refreshToken(refreshToken, response);
	            return ResponseEntity.ok(authResponse);
	        } catch (ApiRequestException ex) {
	            log.error("Token refresh failed: {}", ex.getMessage());
	            return ResponseEntity.status(ex.getStatus())
	                    .body(Map.of("message", ex.getMessage()));
	        }}
	@GetMapping("/status")
	@Operation(security = @SecurityRequirement(name = "bearerAuth"))
	public ResponseEntity<String> getStatus(@AuthenticationPrincipal UserPrincipal user) {
	    log.info("Received request to get status for user: {}", user.getUsername());
	    
	    String status = sr.getStatus(user.getUsername());
	    log.debug("Status retrieved successfully for user: {}", user.getUsername());
	    
	    return new ResponseEntity<String>(status, HttpStatus.OK);
	}
}
