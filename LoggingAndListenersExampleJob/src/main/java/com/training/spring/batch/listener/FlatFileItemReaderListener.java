package com.training.spring.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.stereotype.Component;

import com.training.spring.batch.pojo.CustomerRecord;

@Component
public class FlatFileItemReaderListener implements ItemReadListener<CustomerRecord> {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(FlatFileItemReaderListener.class);

	@Override
	public void beforeRead() {
		LOGGER.info("Start of 'fileRecordReader'");
	}

	@Override
	public void afterRead(CustomerRecord item) {
		LOGGER.info("End of 'fileRecordReader': "+item.toString());
	}

	@Override
	public void onReadError(Exception ex) {
		LOGGER.error("Error on 'fileRecordReader': "+ex.getMessage());		
	}

}
