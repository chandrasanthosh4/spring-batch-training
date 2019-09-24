package com.training.spring.batch.job;

import java.util.Optional;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.training.spring.batch.entity.Customer;
import com.training.spring.batch.exception.ValidationBusinessException;
import com.training.spring.batch.pojo.CustomerRecord;
import com.training.spring.batch.util.CustomerMapper;
import com.training.spring.batch.util.CustomerRecordValidatorUtil;

@Component
public class CustomerRecordProcessor implements ItemProcessor<CustomerRecord, Customer> {
	
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
			System.err.println("Skipping customer record: "+customerRecord.toString());
			throw new ValidationBusinessException(validationResult);
		}
		
		if(customerRecord.getCustomerActiveInd()) {
			System.err.println("Processing customer record: "+customerRecord.toString());
			customer = customerMapper.mapCustomerRecordToEntity(customerRecord);
			return customer;
		}
		System.err.println("Filtering customer record: "+customerRecord.toString());
		//return 'null' from processor to not to pass the inactive customer object to writer
		return null;
	}

}
