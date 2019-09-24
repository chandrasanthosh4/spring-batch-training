package com.training.spring.batch.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.training.spring.batch.entity.Customer;
import com.training.spring.batch.pojo.CustomerRecord;

@Component
public class CustomerMapper {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(CustomerMapper.class);
	
	public Customer mapCustomerRecordToEntity(CustomerRecord cr) {
		LOGGER.info("Start of mapCustomerRecordToEntity");
		Customer c = new Customer();
		c.setCustomerName(cr.getCustomerName());
		c.setCustomerAddress(cr.getCustomerAddress());
		c.setCustomerEmail(cr.getCustomerEmail());
		c.setCustomerMobile(cr.getCustomerMobile());
		c.setCustomerActiveInd(cr.getCustomerActiveInd());
		LOGGER.info("End of mapCustomerRecordToEntity");
		return c;
	}

}
