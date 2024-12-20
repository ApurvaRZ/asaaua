package com.cdac.mumbai.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnsureNumberValidator implements ConstraintValidator<EnsureNumber, Object> {
	 private EnsureNumber ensureNumber;

	    public void initialize(EnsureNumber constraintAnnotation) {
	        this.ensureNumber = constraintAnnotation;
	    }

	    public boolean isValid(Object value, ConstraintValidatorContext context) {
	        // Check the state of the Adminstrator.
	        if (value == null) {
	            return false;
	        }

	        // Initialize it.
	        String regex = ensureNumber.decimal() ? "-?[0-9][0-9\\.\\,]*" : "-?[0-9]+";
	        String data = String.valueOf(value);
	        return data.matches(regex);
	    }
}
