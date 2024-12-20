package com.cdac.mumbai.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IPValidator implements ConstraintValidator<ValidIP, String> {

	private Pattern pattern;
    private Matcher matcher;
    private static final String IP_PATTERN = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
	

public boolean isValid(String value, ConstraintValidatorContext context) {

	 return (validateEmail(value));
}

 public void initialize(ValidIP parameters) {

 }
 private boolean validateEmail(final String ip) {
     pattern = Pattern.compile(IP_PATTERN);
     matcher = pattern.matcher(ip);
     return matcher.matches();
 }


}
