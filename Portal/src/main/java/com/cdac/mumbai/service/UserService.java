package com.cdac.mumbai.service;



import java.util.Locale;

import com.cdac.mumbai.dto.AuthenticationRequest;
import com.cdac.mumbai.dto.AuthenticationResponse;
import com.cdac.mumbai.dto.UserRegistration;
import com.cdac.mumbai.dto.UserUpdation;
import com.cdac.mumbai.jwt.UserPrincipal;
import com.cdac.mumbai.model.User;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {
  boolean userExists(String email);
  String register(UserRegistration user, HttpServletRequest request);
  String updateUserInformation(String string, UserUpdation userUpdation);
  String updateDefaultPassword(String username, String password);
  void createVerificationTokenForUser(User user, String token);
  String validateVerificationToken(String token);
  String generateNewVerificationToken(String existingToken, HttpServletRequest request);
 // User getUser(String verificationToken);
  //User loadUserByUsername(String email);
  Integer getDeptCount();
  String resetPassword(String email, HttpServletRequest request);
  String changePassword(String old, String recent, String username);
  String validateResetToken(String est);
  //void changeUserPassword(User user,  String password);
  AuthenticationResponse login(AuthenticationRequest request, HttpServletResponse response);
  AuthenticationResponse refreshToken(String refreshToken, HttpServletResponse response);
   User getDetails(String username);
  
  
}
