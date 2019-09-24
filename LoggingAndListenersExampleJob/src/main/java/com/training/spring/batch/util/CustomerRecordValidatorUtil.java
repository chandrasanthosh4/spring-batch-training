package com.training.spring.batch.util;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.training.spring.batch.pojo.CustomerRecord;

@Component
public class CustomerRecordValidatorUtil {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(CustomerRecordValidatorUtil.class);

	public String validateCustomerRecord(CustomerRecord customerRecord) {
		LOGGER.info("Start of validateCustomerRecord");
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
		LOGGER.info("End of validateCustomerRecord");
		return validationResult;
	}
}
