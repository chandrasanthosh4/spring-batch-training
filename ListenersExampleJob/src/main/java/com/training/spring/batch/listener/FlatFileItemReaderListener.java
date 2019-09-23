package com.training.spring.batch.listener;

import org.springframework.batch.core.ItemReadListener;
import org.springframework.stereotype.Component;

import com.training.spring.batch.pojo.CustomerRecord;

@Component
public class FlatFileItemReaderListener implements ItemReadListener<CustomerRecord> {

	@Override
	public void beforeRead() {
		System.out.println("Start of 'fileRecordReader'");
	}

	@Override
	public void afterRead(CustomerRecord item) {
		System.out.println("End of 'fileRecordReader': "+item.toString());
	}

	@Override
	public void onReadError(Exception ex) {
		System.err.println("Error on 'fileRecordReader': "+ex.getMessage());		
	}

}
