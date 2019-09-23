package com.training.spring.batch.listener;

import org.springframework.batch.core.SkipListener;
import org.springframework.stereotype.Component;

import com.training.spring.batch.entity.Customer;
import com.training.spring.batch.pojo.CustomerRecord;

@Component
public class ExportFileToDbSkipListener implements SkipListener<CustomerRecord, Customer> {

	@Override
	public void onSkipInRead(Throwable t) {
		System.err.println("Skipping customer record during read for exception: "+t.getMessage());
	}

	@Override
	public void onSkipInWrite(Customer item, Throwable t) {
		System.err.println("Skipping customer entity during write for exception: "+t.getMessage());		
	}

	@Override
	public void onSkipInProcess(CustomerRecord item, Throwable t) {
		System.err.println("Skipping customer record during process for exception: "+t.getMessage());
	}

}
