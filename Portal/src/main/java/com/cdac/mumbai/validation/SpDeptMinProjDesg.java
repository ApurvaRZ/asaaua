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
@Constraint(validatedBy = SpDeptMinProjDesgValidator.class)
@Documented
public @interface SpDeptMinProjDesg {
	 String message() default "Enter alphanumeric character (A-Z a-z 0-9 &,.'-/( ))";

	    Class<?>[] groups() default {};

	    Class<? extends Payload>[] payload() default {};
}
