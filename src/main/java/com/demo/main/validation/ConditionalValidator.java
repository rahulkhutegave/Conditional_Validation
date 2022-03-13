package com.demo.main.validation;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.rmi.UnexpectedException;
import java.util.Arrays;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ConditionalValidator implements ConstraintValidator<ConditionalValidation, Object> {

	private String conditionalProperty;
	private String requiredProperties;
	private String message;
	private String[] values;
	private static final String regexPattern = "^(.+)@(\\S+)$";

	@Override
	public void initialize(ConditionalValidation constraint) {
		conditionalProperty = constraint.conditionalProperty();
		requiredProperties = constraint.requiredProperties();
		message = constraint.message();
		values = constraint.values();
	}

	@Override
	public boolean isValid(Object userInfo, ConstraintValidatorContext context) {
		try {
			Object conditionalPropertyValue = getProperty(userInfo, conditionalProperty);
			if (null == conditionalPropertyValue) {
				return false;
			}
			if (doConditionalValidation(conditionalPropertyValue)) {
				return validateRequiredProperties(userInfo, context);
			}
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | UnexpectedException ex) {
			return false;
		}
		return true;
	}

	private static Object getProperty(Object bean, String propertyName) {

		BeanInfo beanInfo;
		try {
			beanInfo = Introspector.getBeanInfo(bean.getClass());
			PropertyDescriptor propertyDescriptor = Arrays.stream(beanInfo.getPropertyDescriptors())
					.filter(pd -> pd.getName().equals(propertyName)).findFirst().get();
			return propertyDescriptor.getReadMethod().invoke(bean);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private boolean validateRequiredProperties(Object userInfo, ConstraintValidatorContext context)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, UnexpectedException {
		boolean isValid = true;
		if (null != requiredProperties) {
			String type = (String) getProperty(userInfo, conditionalProperty);
			String userEmail = (String) getProperty(userInfo, requiredProperties);
			if (null != type && type.equalsIgnoreCase("User")) {
				isValid = patternMatches(userEmail, regexPattern);
				context.disableDefaultConstraintViolation();
				context.buildConstraintViolationWithTemplate(message).addPropertyNode(requiredProperties)
						.addConstraintViolation();
			}
		}
		return isValid;
	}

	private static boolean patternMatches(String emailAddress, String regexPattern) {
		return Pattern.compile(regexPattern).matcher(emailAddress).matches();
	}

	private boolean doConditionalValidation(Object actualValue) {
		return Arrays.asList(values).contains(actualValue);
	}
}