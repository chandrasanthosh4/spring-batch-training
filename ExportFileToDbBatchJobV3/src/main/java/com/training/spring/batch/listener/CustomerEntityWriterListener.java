package com.training.spring.batch.listener;

import java.util.List;

import org.springframework.batch.core.ItemWriteListener;
import org.springframework.stereotype.Component;

import com.training.spring.batch.entity.Customer;

@Component
public class CustomerEntityWriterListener implements ItemWriteListener<Customer> {

	@Override
	public void beforeWrite(List<? extends Customer> items) {
		System.out.println("Start of 'dbRecordWriter': "+items.size());
	}

	@Override
	public void afterWrite(List<? extends Customer> items) {
		System.out.println("End of 'dbRecordWriter': "+items.size());
	}

	@Override
	public void onWriteError(Exception exception, List<? extends Customer> items) {
		System.err.println("Error occurred during save of Customer entity to DB: "+exception.getMessage());
	}

}
