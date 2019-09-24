package com.training.spring.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.stereotype.Component;

import com.training.spring.batch.entity.Customer;
import com.training.spring.batch.pojo.CustomerRecord;

@Component
public class CustomerRecordProcessorListener implements ItemProcessListener<CustomerRecord, Customer> {

	public static final Logger LOGGER = LoggerFactory.getLogger(CustomerRecordProcessorListener.class);
	
	@Override
	public void beforeProcess(CustomerRecord item) {
		LOGGER.info("Start of 'CustomerRecordProcessor' for customer record: "+item.toString());
	}

	@Override
	public void afterProcess(CustomerRecord item, Customer result) {
		LOGGER.info("End of 'CustomerRecordProcessor'");
	}

	@Override
	public void onProcessError(CustomerRecord item, Exception e) {
		LOGGER.error("Error : "+e.getMessage()+" occurred while processing the CustomerRecord "+item.toString());
	}

}
