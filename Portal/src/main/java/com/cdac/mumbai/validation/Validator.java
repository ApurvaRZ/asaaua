package com.cdac.mumbai.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

 public boolean validateEmail(String email) {
	 //String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";   
	 String regex = "^[A-Za-z0-9.!#$%&*+/=?^_`'{|}~-]{3,}@[a-zA-Z.-]{3,}([.]{1}[a-zA-Z]{2,}|[.]{1}[a-zA-Z]{2,}[.]{1}[a-zA-Z]{2,})$";
	    Pattern pattern = Pattern.compile(regex);	     
	    Matcher matcher = pattern.matcher(email);
	 return matcher.matches();
 }
 
 public boolean validatePassword(String password) {	      
	 	  final String PASSWORD_PATTERN =
	              "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?!.*\\s).{8,15})";	 	 	 	 
	 	    Pattern pattern = Pattern.compile(PASSWORD_PATTERN);	 	     
	 	        Matcher matcher = pattern.matcher(password);
	 	 return matcher.matches();
	  }

 public boolean validatePhone(String phone) {	      
	 	  final String PHONE_PATTERN = "[0-9]{10,11}";	
	 	    Pattern pattern = Pattern.compile(PHONE_PATTERN);	 	     
	 	    Matcher matcher = pattern.matcher(phone);
	 	 return matcher.matches();
	  }
 
 public boolean validateContactPersonName(String name) {	      
	  final String NAME_PATTERN = "^[A-Za-z\\p{L} \\.']+$";	
	    Pattern pattern = Pattern.compile(NAME_PATTERN);	 	     
	    Matcher matcher = pattern.matcher(name);
	 return matcher.matches();
 }
 public boolean validateContactPersonDesignation(String designation) {	      
	  //final String DESIGNATION_PATTERN = "^(?!\\d+\b)[A-Za-z\\d\\p{L}\\& \\, \\.'\\-\\()/]+$";	
	  final String DESIGNATION_PATTERN = "(?!\\d+\\b)[A-Za-z\\d\\p{L}\\& \\, \\.'\\-]+";
	    Pattern pattern = Pattern.compile(DESIGNATION_PATTERN);	 	     
	    Matcher matcher = pattern.matcher(designation);
	 return matcher.matches();
}
 public boolean validateContactPersonMobNumber(String number) {	      
	  final String MOB_PATTERN = "^((?!(0))[0-9]{10})$";	
	    Pattern pattern = Pattern.compile(MOB_PATTERN);	 	     
	    Matcher matcher = pattern.matcher(number);
	 return matcher.matches();
} 
 public boolean validateAuaKua(String auakua) {	   
	  final String AUA_PATTERN = "^[A-Za-z\\d\\p{L}]{10}$";	
	    Pattern pattern = Pattern.compile(AUA_PATTERN);	 	     
	    Matcher matcher = pattern.matcher(auakua);
	 return matcher.matches();
 }
 public boolean validateUserName(String username) {	      
	  final String AUA_PATTERN = "^[A-Za-z\\d\\p{L}]{8}$";	
	    Pattern pattern = Pattern.compile(AUA_PATTERN);	 	     
	    Matcher matcher = pattern.matcher(username);
	 return matcher.matches();
}
 
}
