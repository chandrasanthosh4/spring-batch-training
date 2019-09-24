package com.training.spring.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class ExportFileToDbStepExecListener implements StepExecutionListener {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ExportFileToDbStepExecListener.class);

	@Override
	public void beforeStep(StepExecution stepExecution) {
		LOGGER.info("Start of 'ExportFileToDbBatchStep'");
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		LOGGER.info("End of 'ExportFileToDbBatchStep'");
		return stepExecution.getExitStatus();
	}

}
