package com.training.spring.batch.job;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.training.spring.batch.entity.Customer;
import com.training.spring.batch.pojo.CustomerRecord;
import com.training.spring.batch.util.CustomerMapper;

@Component
public class CustomerRecordProcessor implements ItemProcessor<CustomerRecord, Customer> {

	@Autowired
	private CustomerMapper customerMapper;

	@Override
	public Customer process(CustomerRecord customerRecord) throws Exception {
		Customer customer;
		customer = customerMapper.mapCustomerRecordToEntity(customerRecord);
		return customer;
	}

}
