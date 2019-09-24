package com.training.spring.batch.util;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.training.spring.batch.entity.Customer;
import com.training.spring.batch.exception.ValidationBusinessException;
import com.training.spring.batch.pojo.CustomerRecord;

@Component
public class CustomerRecordProcessor implements ItemProcessor<CustomerRecord, Customer> {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(CustomerRecordProcessor.class);
	
	@Autowired
	private CustomerRecordValidatorUtil customerRecordValidatorUtil; 
	
	@Autowired
	private CustomerMapper customerMapper;

	@Override
	public Customer process(CustomerRecord customerRecord) throws Exception {
		Customer customer;
		
		//validate the customer record build from the input file
		String validationResult = customerRecordValidatorUtil.validateCustomerRecord(customerRecord);
		
		//throw a runtime error to skip the respective customer and proceed to next record
		if(Optional.ofNullable(validationResult).isPresent()) {
			LOGGER.error("Skipping customer record: "+customerRecord.toString());
			throw new ValidationBusinessException(validationResult);
		}
		
		if(customerRecord.getCustomerActiveInd()) {
			LOGGER.info("Processing customer record: "+customerRecord.toString());
			customer = customerMapper.mapCustomerRecordToEntity(customerRecord);
			return customer;
		}
		LOGGER.warn("Filtering customer record: "+customerRecord.toString());
		//return 'null' from processor to not to pass the inactive customer object to writer
		return null;
	}

}
