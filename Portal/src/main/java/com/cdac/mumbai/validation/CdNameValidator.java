package com.cdac.mumbai.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;



public class CdNameValidator implements ConstraintValidator<CdName, String> {
    private Pattern pattern;
    private Matcher matcher;
    private static final String NAME_PATTERN = "^[A-Za-z\\p{L} \\.']+$";

    @Override
    public void initialize(final CdName constraintAnnotation) {
    }

    @Override
    public boolean isValid(final String username, final ConstraintValidatorContext context) {
        return (validateName(username));
    }

    private boolean validateName(final String name) {
        pattern = Pattern.compile(NAME_PATTERN);
        matcher = pattern.matcher(name);
        return matcher.matches();
    }

	
}
