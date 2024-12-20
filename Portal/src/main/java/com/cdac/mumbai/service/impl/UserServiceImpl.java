package com.cdac.mumbai.service.impl;

import org.apache.commons.codec.binary.Base64;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cdac.mumbai.common.CredentialsForUser;
import com.cdac.mumbai.common.GeneralMethods;
import com.cdac.mumbai.common.OnRegistrationCompleteEvent;
import com.cdac.mumbai.common.RSACipher;
import com.cdac.mumbai.common.SendMail;
import com.cdac.mumbai.dao.Pr_AgreementRepository;
import com.cdac.mumbai.dao.UserProfileRepository;
import com.cdac.mumbai.dao.UserRepository;
import com.cdac.mumbai.dao.VerificationTokenRepository;
import com.cdac.mumbai.dto.AuthenticationRequest;
import com.cdac.mumbai.dto.AuthenticationResponse;
import com.cdac.mumbai.dto.UserRegistration;
import com.cdac.mumbai.dto.UserUpdation;
import com.cdac.mumbai.exception.ApiRequestException;

import com.cdac.mumbai.exception.GeneralException;
import com.cdac.mumbai.exception.ResourceNotFoundException;
import com.cdac.mumbai.jwt.JwtHelper;
import com.cdac.mumbai.jwt.UserPrincipal;
import com.cdac.mumbai.mapper.CommonMapper;
import com.cdac.mumbai.model.Pr_Agreement;
import com.cdac.mumbai.model.SaConfigPara;
import com.cdac.mumbai.model.User;
import com.cdac.mumbai.model.VerificationToken;
import com.cdac.mumbai.service.SaConfigParaService;
import com.cdac.mumbai.service.UserService;
import com.cdac.mumbai.validation.Validator;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;

import static com.cdac.mumbai.constants.Message.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.StringTokenizer;
import java.util.UUID;

@Service
@Transactional
@Slf4j
@PropertySource("classpath:messages_mail.properties")
public class UserServiceImpl implements UserService{
	//@Autowired
	// AuthenticationManager authenticationManager;
	
	@Autowired
	 JwtHelper jwt ;

	@Autowired
	  UserRepository ur;
	  
//	 @Autowired
//		CommonMapper cm;
	 
	 @Autowired
		ModelMapper mm;
	 
	 @Autowired
	  UserProfileRepository up;
	 
	 @Autowired
	  PasswordEncoder p;
	 
	 @Autowired
	 VerificationTokenRepository vr;
	 
	 @Autowired
		ApplicationEventPublisher eventPublisher;
	 
	 @Autowired 
		SaConfigParaService sa;
	 
	 @Autowired 
	 Pr_AgreementRepository pr;
	 
	 @Autowired
	 AuthenticationManager authenticationManager;
	 
	 @Autowired
	 SendMail sm;
	 
	 @Autowired
	 RSACipher rsa = new RSACipher();
	
	 String url = null;
	 String token = null;
	
	

	 @Override
	 public String register(UserRegistration user, HttpServletRequest request) {
	     log.info("Registration started for email: {}", user.getCdemail());

	     if (ur.findByCdemail(user.getCdemail()) != null) {
	         log.error("An account for the email {} already exists.", user.getCdemail());
	         throw new GeneralException(EMAIL_IN_USE);
	     }

	     User usr = mm.map(user, User.class);
	     log.info("User entity mapped for email: {}", user.getCdemail());

	     usr.setUpdate_timestamp(new Date(System.currentTimeMillis()));
	     usr.setRegistration_timestamp(new Date(System.currentTimeMillis()));

	     if ("aua".equalsIgnoreCase(user.getService_type())) {
	         String saUsername = CredentialsForUser.getUsername("SUB");
	         log.info("Username for SUBAUA Client: {}", saUsername);
	         usr.setRoles(up.findByType("USER"));
	         usr.setUsername(saUsername);
	         usr.setUpdate_by(saUsername);
	     } else if ("asa".equalsIgnoreCase(user.getService_type())) {
	         String auaUsername = CredentialsForUser.getUsername("AUA");
	         log.info("Username for AUA Client: {}", auaUsername);
	         usr.setRoles(up.findByType("AUAUSER"));
	         usr.setUsername(auaUsername);
	         usr.setUpdate_by(auaUsername);
	     }

	     String generatedPassword = CredentialsForUser.genPassword();
	     log.info("Password generated for user: {}", generatedPassword);
	     usr.setPassword(generatedPassword);
	     usr.setStatus("NR");
	     
	     log.info("Organization name : {} ", usr.getOrganizationName());

	     ur.save(usr);
	     log.info("User {} successfully registered and saved in the database.", user.getCdemail());

	     try {
	         // Publish registration event to send confirmation email
	         eventPublisher.publishEvent(new OnRegistrationCompleteEvent(
	                 usr,
	                 request.getLocale(),
	                 sa.getSaConfigKeys("email_encrypt_public_key").getBytes(),
	                 new GeneralMethods().getAppUrl(request)
	         ));

	         return "Registration successful. A confirmation email has been sent to " + user.getCdemail();

	     } catch (Exception e) {
	         log.error("Registration successful, but failed to send confirmation email: {}", e.getMessage());
	         return "Registration successful, but we couldn't send a confirmation email. Please contact support.";
	     }
	 }

	@Override
	public String updateUserInformation(String username, UserUpdation userUpdation) {
		log.info("User Information Updation Started");
		 User usr = ur.findByUsername(username)
	                .orElseThrow(() -> new ApiRequestException(USERNAME_NOT_FOUND, HttpStatus.NOT_FOUND));
		  // Use ModelMapper to map and merge selected fields from the UserUpdation DTO to the User entity
       
		 System.out.println("User Object : "+usr.toString());
           
        // Save the updated user entity back to the database
		    boolean isValidphone = false;
			isValidphone = new Validator().validatePhone(userUpdation.getPhone());
			boolean isValidEmail = false;
			isValidEmail = new Validator().validateEmail(userUpdation.getCd_email());
			boolean isValidCdName = false;
			isValidCdName = new Validator().validateContactPersonName(userUpdation.getCd_name());
			boolean isValidDesignation = false;
			isValidDesignation = new Validator().validateContactPersonDesignation(userUpdation.getCd_designation());
			boolean isValidMobNum = false;
			isValidMobNum = new Validator().validateContactPersonMobNumber(userUpdation.getCd_mobile());
			
			if (!isValidphone) {
				log.error("Phone no. is not valid : ",userUpdation.getPhone());
				return "Phone no. is not valid";
			}
			if (!isValidEmail) {
				log.error("Email is not valid : ",userUpdation.getCd_email());
				return "Email is not valid";
			}
			if (!isValidCdName) {
				log.error("Name is not valid : ",userUpdation.getCd_name());
				return "Name is not valid";
			}
			if (!isValidDesignation) {
				log.error("Designation is not valid : ",userUpdation.getCd_designation());
				return "Designation is not valid";
			}
			if (!isValidMobNum) {
				log.error("Designation is not valid : ",userUpdation.getCd_mobile());
				return "Mobile no. is not valid";
			}
			
		//	System.out.println("dfghjfjhyui"+usr.getCdemail());
			//System.out.println("dfghjfjhyui   gfcgvhgbj   "+ur.findByCdemail(usr.getCdemail()));
			if (ur.findByCdemail(userUpdation.getCd_email()) != null) {
				//if (ur.findByCdemail(user.getCdemail()) != null)
				log.error("Email is aleady in use");
	            throw new GeneralException(EMAIL_IN_USE);
	        }
			
			  usr.setCd_designation(userUpdation.getCd_designation());
			  usr.setCdemail(userUpdation.getCd_email());
			  usr.setPhone(userUpdation.getPhone());
			  usr.setCd_mobile(userUpdation.getCd_mobile());
			  usr.setCd_name(userUpdation.getCd_name());
			  
			  
			//  usr =  mm.map(userUpdation,User.class);
			  
			  System.out.println("dfghjkl  "+usr.getAddress());
			  
			  
			  ur.save(usr);
			  
			  log.info("Successfully updated user : ");
			
			 String success = sm.updateProfileEmail(userUpdation);
			 
			 log.info("Send mail Status : ",success);
			 
		return success;// cm.mapToDto(ur.save(usr),UserUpdation.class);
	}

	@Override
	public boolean userExists(String email) {
	    log.info("Checking if user exists with email: {}", email);
	    
	    try {
	        // Assuming UserRepository has a method to find a user by email
	        boolean exists = ur.existsByCdemail(email);
	        
	        log.debug("User with email: {} exists: {}", email, exists);
	        return exists;
	    } catch (Exception e) {
	        log.error("Error occurred while checking if user exists with email: {}. Exception: {}", email, e.getMessage());
	        return false;
	    }
	}


	@Override
	public void createVerificationTokenForUser(User user, String token) {
		log.info("Inside createVerificationTokenForUser",user.toString(),"\n TOken ",token);
		
		 VerificationToken myToken = new VerificationToken(token, user);
		
		  log.info("Verification Token : ",myToken.toString());
		 vr.save(myToken);
		 
		
	}

	@Override
	public String validateVerificationToken(String est) {

	    log.info("Est Text: {}", est);
	    String url = null;
	    String token = null;

	    try {
	        // Decrypt the token
	        url = rsa.decrypt(est.replace(" ", "+"));//, Base64.decodeBase64(sa.getSaConfigKeys("email_decrypt_private_key").getBytes()));
	        log.info("Decrypted URL: {}", url);
	    } catch (IOException | GeneralSecurityException e) {
	        log.error("Exception occurred during decryption: {}", e.getMessage());
	        e.printStackTrace();
	        return "Error occurred during decryption.";
	    }

	    // Extract the token from the decrypted URL
	    StringTokenizer stringTokenizer = new StringTokenizer(url, "&=");
	    log.info("Tokenizing URL...");
	    while (stringTokenizer.hasMoreElements()) {
	        token = stringTokenizer.nextElement().toString();
	        log.info("Extracted Token: {}", token);
	    }

	    VerificationToken verification = vr.findByToken(token);
	    
	    log.info("Verification Token: {}", verification);

	    if (verification == null) {
	        log.error("Verification token is null or invalid.");
	        return TOKEN_INVALID;
	    }

	    if (verification.getTokenId() == null) {
	        log.error("Verification token ID is invalid.");
	        return TOKEN_INVALID;
	    }

	    Calendar cal = Calendar.getInstance();
	    if (verification.getExpiryDate().getTime() - cal.getTime().getTime() <= 0) {
	        if (!"NR".equals(verification.getUser().getStatus())) {
	            log.error("Token expired and is not for a new request.");
	            return TOKEN_INVALID;
	        }
	        log.error("Token has expired.");
	        return TOKEN_EXPIRED;
	    }

	    // Send confirmation email and handle responses
	    String emailSendStatus = sm.confirmationEmail(verification);
	    log.info("Email Sending Status: {}", emailSendStatus);

	    if ("Failed to send confirmation email to user.".equals(emailSendStatus)) {
	        return "Failed to send confirmation email to the user.";
	    } else if ("Confirmation email sent to user but failed to send to admin.".equals(emailSendStatus)) {
	        return "Confirmation email sent to the user, but failed to send to the admin.";
	    } else if ("Error occurred while sending emails.".equals(emailSendStatus)) {
	        return "Error occurred while sending emails.";
	    } else if ("Confirmation emails successfully sent to both user and admin.".equals(emailSendStatus)) {
	        // Proceed with setting the user's status and removing the token
	        verification.getUser().setStatus("FS");
	        verification.getUser().setEmail_verification_timestamp(new Date(System.currentTimeMillis()));
	        

	        // Delete the verification token after successful email confirmation
	    //    vr.deleteByTokenId(verification.getTokenId());
	      //  vr.delete(verification);
	     //   log.info("Verification token deleted.");
	        verification.getUser().setPassword(p.encode(verification.getUser().getPassword()));
	        // Create a new agreement and save it
	        Pr_Agreement p = new Pr_Agreement();
	        p.setAgreement_status("NEW");
	        p.setAuasubaua(verification.getUser());
	        p.setUpdate_by(verification.getUser().getUsername());
	        p.setUpdate_timestamp(new Date(System.currentTimeMillis()));
	        pr.save(p);

	        return "Emails sent successfully, verification completed.";
	    }

	    // Catch any unhandled case (this should ideally not be reached)
	    return "Unexpected error occurred.";
	}

	
//	public byte[] getSaConfigKeys(String ex) {
//		// SaConfigPara sakeys =  (SaConfigPara) 
//		//		List xyz =  sa.getSaConfigKeys(ex);
//			//	for (int i = 0; i<=xyz.size();i++) {
//				//System.out.println("list element"+xyz.get(i));}
//			//	SaConfigPara sakeys =  (SaConfigPara) sa.getSaConfigKeys(ex);
//	System.out.println("in get byte"+sa.getSaConfigKeys(ex));
//		return sa.getSaConfigKeys(ex).getBytes();
//	}

//	public String generateNewVerificationToken(String existingToken,HttpServletRequest request) {
		

//	@Override
//	public User getUser(String verificationToken) {
//		VerificationToken verification = vr.findByToken(token);
//		if (verification != null) {
//			User usr = verification.getUser();
//		}
//		
//		return null;
//	}
 


	@Override
	public String generateNewVerificationToken(String existingToken, HttpServletRequest request) {
		log.info("Inside generateNewVerificationToken");
		VerificationToken verification = vr.findByToken(token);
		if (verification != null) {
			
            verification.updateToken(UUID.randomUUID().toString());
             vr.save(verification);
             log.info("New token gets generated and saved");
             }
            String success = sm.resendTokenEmail(verification, request);
            
            log.info("Mail Sending Status : ",success);
            
            if ("failtosendmail".equals(success)) {
                return "failtosendmail";
            } 
		
            return "successtosendmail";
	}

	@Override
	public Integer getDeptCount() {
		
		return ur.deptCount();
	}

	@Override
	public String resetPassword(String email, HttpServletRequest request) {
		 log.info("Inside Reset Password  : ",email);
		boolean isValidEmail = false;
		isValidEmail = new Validator().validateEmail(email);
		if(!isValidEmail) {
			log.error("Email Validation failed");
		return "Return forget password page"; }
		
		else {
			User usr = ur.findByCdemail(email);
			
			if(usr == null) {
				log.error("No user found for this mail id");
				return "Return forget password page";}
			
				String token = UUID.randomUUID().toString();
				VerificationToken vt = new VerificationToken(token, usr);
				vr.save(vt);
				log.info("Token Saved");
				 String success = sm.resendTokenEmail4Resetpass(vt, request);
				 log.info("Mail Sending Status : ",success);
				 if ("failtosendmail".equals(success)) {
		                return "failtosendmail and return to forgot password page";
		            } 
				
		            return "successtosendmail";
			
		}
	}

	@Override
	public String changePassword(String recent, String old, String username) {
		log.info("Inside changePassword");
		boolean isValidPassword = false;
		isValidPassword = new Validator().validatePassword(old);
		if (!isValidPassword) {
			log.error("Invalid Old Password"); 
			return "portalpasswordvalidation";
		}
		isValidPassword = new Validator().validatePassword(recent);
		if (!isValidPassword) {
			log.error("Invalid New Password"); 
			return "portalpasswordvalidation";
		} 
		
		 User usr = ur.findByUsername(username)
	                .orElseThrow(() -> new ApiRequestException(USERNAME_NOT_FOUND, HttpStatus.NOT_FOUND));
		//Optional<User> usr = ur.findByUsername(r);
		// System.out.println("rtyui "+usr.toString());
		 
		 log.info("user entered old and new password {} and {} ", p.encode(old), 	usr.getPassword());	
		 
		 if (!p.matches(old, usr.getPassword())) {
			    log.error("Password validation failed for user: {}", usr.getUsername());
			    return "The current password entered is incorrect";
			}
		if (old.equals(recent)) {
			log.error("Old Password and New Password is same"); 
			return "Old Password and New Password is same";
			

		}
		 usr.setPassword(p.encode(recent));
		// System.out.println("asertyu cdfgh "+recent);
		 //System.out.println("asertyu"+usr.getPassword());
		 ur.save(usr);
		 log.info("Successfully set Password");
		String success = sm.updatePassEmail(usr);
		log.info("Mail Sending Status : ",success);
		
		 if ("failtosendmail".equals(success)) {
             return "failtosendmail";
         } else {
		  
         return "successtosendmail";
	}  
		
		 
		 
	}

	@Override
	public String validateResetToken(String est) {
		
		log.info("Inside validateResetToken");
	       try {
				url = rsa.decrypt(est.replace(" ", "+"));//, Base64.decodeBase64(sa.getSaConfigKeys("email_decrypt_private_key").getBytes()));
			} catch (IOException | GeneralSecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 

		StringTokenizer stringTokenizer = new StringTokenizer(url, "&=");
		while (stringTokenizer.hasMoreElements()) {
		
		  token = stringTokenizer.nextElement().toString();}
		VerificationToken verification = vr.findByToken(token);
		if(verification.equals(null)) {
			return TOKEN_INVALID;
			
		}
		
		if(verification.getTokenId().equals(null)) {
			return TOKEN_INVALID;
		}
		
		Calendar cal = Calendar.getInstance();

     if (verification.getExpiryDate().getTime() - cal.getTime().getTime() <= 0) {
         if (!"NR".equals(verification.getUser().getStatus())) {
             return TOKEN_INVALID;
         }
         return TOKEN_EXPIRED;
     }
     
  

  //   verification.getUser().setStatus("ACTIVE");
     //vr.delete(verification);
     
    // vr.deleteByTokenId(verification.getTokenId());
   
     return verification.getUser().getCdemail();

    
		//return email;
	}

//	@Override
//	public AuthenticationResponse login(AuthenticationRequest request) {
//		 try {
//			 System.out.println("request "+request.getUsername());
//			 System.out.println("request "+request.getPassword());
//			 authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//						request.getUsername(), request.getPassword()));
//			 System.out.println("request after auth");
//			 User user = ur.findByUsername(request.getUsername()).orElseThrow(() -> new ApiRequestException(USERNAME_NOT_FOUND, HttpStatus.NOT_FOUND));
//			 System.out.println("request after auth"+user.getCdemail());
//	String token = jwt.generateToken(user);
//	System.out.println("request after auth"+token);
//	AuthenticationResponse res = new AuthenticationResponse();
//	
//	res.setToken(token);
//		return res;
//	 } catch (AuthenticationException e) {
//         throw new ApiRequestException(INCORRECT_PASSWORD, HttpStatus.FORBIDDEN);
//     }
//	}

	 @Override
	    public AuthenticationResponse login(AuthenticationRequest request, HttpServletResponse response) {
	        try {
	            authenticationManager.authenticate(
	                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
	            );

	            User user = ur.findByUsername(request.getUsername())
	                    .orElseThrow(() -> new ApiRequestException("User not found", HttpStatus.NOT_FOUND));

	            // Ensure email is verified
	            if (user.getEmail_verification_timestamp() == null) {
	                throw new ApiRequestException("Email not verified", HttpStatus.UNAUTHORIZED);
	            }

	            // Generate tokens
	            String accessToken = jwt.generateAccessToken(user);
	            String refreshToken = jwt.generateRefreshToken(user);

	            // Set refresh token as HTTP-only cookie
	            setRefreshTokenCookie(refreshToken, response);

	            return new AuthenticationResponse(accessToken, jwt.getAccessTokenExpiry());
	        } catch (AuthenticationException e) {
	            throw new ApiRequestException("Invalid credentials", HttpStatus.FORBIDDEN);
	        }
	    }

	    @Override
	    public AuthenticationResponse refreshToken(String refreshToken, HttpServletResponse response) {
	        if (jwt.isTokenExpired(refreshToken)) {
	            throw new ApiRequestException("Refresh token expired", HttpStatus.UNAUTHORIZED);
	        }

	        String username = jwt.getUsernameFromToken(refreshToken);
	        User user = ur.findByUsername(username)
	                .orElseThrow(() -> new ApiRequestException("User not found", HttpStatus.NOT_FOUND));

	        // Generate new access token
	        String newAccessToken = jwt.generateAccessToken(user);

	        // Return new tokens
	        return new AuthenticationResponse(newAccessToken, jwt.getAccessTokenExpiry());
	    }

	    private void setRefreshTokenCookie(String refreshToken, HttpServletResponse response) {
	        Cookie cookie = new Cookie("refresh_token", refreshToken);
	        cookie.setHttpOnly(true);
	        cookie.setSecure(true); // Ensure HTTPS usage
	        cookie.setPath("/");
	        cookie.setMaxAge((int) JwtHelper.REFRESH_TOKEN_VALIDITY_SECONDS);
	        response.addCookie(cookie);
	    }
	@Override
	public String updateDefaultPassword(String email, String password) {
		log.info("Inside changePassword");
		boolean isValidPassword = false;
		isValidPassword = new Validator().validatePassword(password);
		if (!isValidPassword) {
			log.error("Invalid Password"); 
			return "portalpasswordvalidation";
		}
//		 User usr = ur.findByUsername(username)
//	                .orElseThrow(() -> new ApiRequestException(USERNAME_NOT_FOUND, HttpStatus.NOT_FOUND));
		User usr = ur.findByCdemail(email);
		
		if(usr == null) {
			log.error("No user found for this mail id");
			return "Return forget password page";}
		
		 usr.setPassword(p.encode(password));
		 usr.setSet_password_flag(true);
		 usr.setReset_password_timestamp(new Date(System.currentTimeMillis()));
		 ur.save(usr);
		 String success = sm.resetPassword(usr);
		 log.info("Mail Sending Status : {} ",success);
			
		 if ("failtosendmail".equals(success)) {
			 
             return "failtosendmail";
         } else {
		  
         return "successtosendmail";
	}

//	@Override
//	public void changeUserPassword(User user, String password) {
//		user.setPassword(p.encode(password));
//		
//	}

//	@Override
//	public User loadUserByUsername(String email) {
//		
//		return ur.findByCdemail(email);
//	}
	}

	@Override
	public User getDetails(String username) {
		 return ur.findByUsername(username)
	                .orElseThrow(() -> new ApiRequestException(USERNAME_NOT_FOUND, HttpStatus.NOT_FOUND));
		//return usr;
	}}

