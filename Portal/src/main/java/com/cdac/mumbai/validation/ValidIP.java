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
@Constraint(validatedBy = IPValidator.class)
@Documented
public @interface ValidIP {
	String message() default "{ValidIP.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
