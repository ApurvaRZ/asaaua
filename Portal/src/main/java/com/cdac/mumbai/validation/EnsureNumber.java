package com.cdac.mumbai.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.FIELD, ElementType.TYPE} )
@Constraint(validatedBy = EnsureNumberValidator.class)
@Documented
public @interface EnsureNumber {
	 String message() default "{PasswordMatch}";

	    Class<?>[] groups() default {};

	    Class<? extends Payload>[] payload() default {};

	    boolean decimal() default false;

}
