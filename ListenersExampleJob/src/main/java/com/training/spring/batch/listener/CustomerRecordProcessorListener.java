package com.training.spring.batch.listener;

import org.springframework.batch.core.ItemProcessListener;
import org.springframework.stereotype.Component;

import com.training.spring.batch.entity.Customer;
import com.training.spring.batch.pojo.CustomerRecord;

@Component
public class CustomerRecordProcessorListener implements ItemProcessListener<CustomerRecord, Customer> {

	@Override
	public void beforeProcess(CustomerRecord item) {
		System.out.println("Start of 'CustomerRecordProcessor' for customer record: "+item.toString());
	}

	@Override
	public void afterProcess(CustomerRecord item, Customer result) {
		System.out.println("End of 'CustomerRecordProcessor'");
	}

	@Override
	public void onProcessError(CustomerRecord item, Exception e) {
		System.err.println("Error : "+e.getMessage()+" occurred during processing of CustomerRecord "+item.toString());
	}

}
