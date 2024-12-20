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
@Constraint(validatedBy = FeedbackMessageValidator.class)
@Documented
public @interface FeedbackMessage {
	String message() default "Enter alphanumeric character upto 1000(A-Z a-z 0-9 &,.'-/( )?)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
