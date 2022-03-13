package com.demo.main.validation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import com.demo.main.entity.User;

@SpringBootTest
@AutoConfigureMockMvc
public class UserValidationTest {

	private Validator validator;

	private User user = User.builder().build();

	@BeforeEach
	public void setUp() throws Exception {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
		validator = validatorFactory.getValidator();
		user.setName("User_Or_Team_name");
	}

	@Test
	public void validationExceptionShouldTrigger_constraintViolations() {
		user.setType("User");
		user.setEmail("Invalid_Email");
		Set<String> messages = messages(validator.validate(user));

		assertEquals(messages.size(), 1);
		assertTrue(messages.contains("Email Should be valid"));
	}

	@Test
	public void validationExShouldNotTrigger_constraintViolations() {
		user.setType("Team");
		//Email is not provided for team
		Set<String> messages = messages(validator.validate(user));

		assertEquals(messages.size(), 0);
	}

	private Set<String> messages(Set<ConstraintViolation<Object>> constraintViolations) {
		return constraintViolations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());
	}

}
