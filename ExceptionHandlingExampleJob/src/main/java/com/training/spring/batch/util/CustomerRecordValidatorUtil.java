package com.training.spring.batch.util;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.stereotype.Component;

import com.training.spring.batch.pojo.CustomerRecord;

@Component
public class CustomerRecordValidatorUtil {

	public String validateCustomerRecord(CustomerRecord customerRecord) {
		String validationResult = null;
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		
		Set<ConstraintViolation<Object>> violations = validator.validate(customerRecord);
		if (!violations.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			for (ConstraintViolation<Object> constraintViolation : violations) {
				sb.append(constraintViolation.getMessage()).append(". ");
			}
			validationResult = sb.toString();
		}
		return validationResult;
	}
}
