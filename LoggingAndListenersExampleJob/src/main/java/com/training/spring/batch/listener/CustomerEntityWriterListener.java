package com.training.spring.batch.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.stereotype.Component;

import com.training.spring.batch.entity.Customer;

@Component
public class CustomerEntityWriterListener implements ItemWriteListener<Customer> {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(CustomerEntityWriterListener.class);

	@Override
	public void beforeWrite(List<? extends Customer> items) {
		LOGGER.info("Start of 'dbRecordWriter': "+items.size());
	}

	@Override
	public void afterWrite(List<? extends Customer> items) {
		LOGGER.info("End of 'dbRecordWriter': "+items.size());
	}

	@Override
	public void onWriteError(Exception exception, List<? extends Customer> items) {
		LOGGER.error("Error occurred during save of Customer entity to DB: "+exception.getMessage());
	}

}
