package com.cdac.mumbai.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SpDeptMinProjDesgValidator implements ConstraintValidator<SpDeptMinProjDesg, String> {

	 private Pattern pattern;
	    private Matcher matcher;
	    private static final String NAME_PATTERN = "^(?!\\d+\b)[A-Za-z\\d\\p{L}\\& \\, \\.'\\-\\()/]+$";

	    @Override
	    public void initialize(final SpDeptMinProjDesg constraintAnnotation) {
	    }

	    @Override
	    public boolean isValid(final String name, final ConstraintValidatorContext context) {
	        return (validateName(name));
	    }

	    private boolean validateName(final String name) {
	        pattern = Pattern.compile(NAME_PATTERN);
	        matcher = pattern.matcher(name);
	        return matcher.matches();
	    }
}
