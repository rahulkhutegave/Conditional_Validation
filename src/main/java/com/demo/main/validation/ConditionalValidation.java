package com.demo.main.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Repeatable(ConditionalValidations.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { ConditionalValidator.class })
public @interface ConditionalValidation {

	String message() default "Email Should be valid";
	
	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	String conditionalProperty();

	String[] values();

	String requiredProperties();

}
