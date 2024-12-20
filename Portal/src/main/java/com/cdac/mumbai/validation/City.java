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
@Constraint(validatedBy = CityValidator.class)
@Documented
public @interface City {

	String message() default "Enter alphabets upto 50(A-Z , a-z)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
