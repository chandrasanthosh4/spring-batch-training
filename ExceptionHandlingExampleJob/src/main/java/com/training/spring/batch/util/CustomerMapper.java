package com.training.spring.batch.util;

import org.springframework.stereotype.Component;

import com.training.spring.batch.entity.Customer;
import com.training.spring.batch.pojo.CustomerRecord;

@Component
public class CustomerMapper {
	
	public Customer mapCustomerRecordToEntity(CustomerRecord cr) {
		Customer c = new Customer();
		c.setCustomerName(cr.getCustomerName());
		c.setCustomerAddress(cr.getCustomerAddress());
		c.setCustomerEmail(cr.getCustomerEmail());
		c.setCustomerMobile(cr.getCustomerMobile());
		c.setCustomerActiveInd(cr.getCustomerActiveInd());
		return c;
	}

}
