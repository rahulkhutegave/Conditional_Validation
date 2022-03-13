package com.demo.main.entity;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import com.demo.main.validation.ConditionalValidation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ConditionalValidation(conditionalProperty = "type", values = {
		"User" }, requiredProperties = "email", message = "Email Should be valid")
public class User implements Serializable {

	private static final long serialVersionUID = -2020071563261966997L;

	private String id;

	@NotEmpty(message = "Name should not be blank")
	private String name;

	private String email;

	/**
	 * type can be team or user, only user have the email
	 */
	@NotEmpty(message = "Type should not be blank")
	private String type;

}
