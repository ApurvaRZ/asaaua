package com.cdac.mumbai.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class OrgNameAddValidator implements ConstraintValidator<OrgNameAdd, String> {

    private Pattern pattern;
    private Matcher matcher;
    private static final String NAME_PATTERN = "^(?!\\d+\b)[A-Za-z\\d\\p{L}\\& \\, \\.'\\-\\()\"\"/]+$";

    @Override
    public void initialize(final OrgNameAdd constraintAnnotation) {
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
