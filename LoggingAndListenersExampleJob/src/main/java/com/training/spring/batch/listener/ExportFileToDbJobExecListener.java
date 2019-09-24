package com.training.spring.batch.listener;

import org.jboss.logging.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * JobExection is usually needed if we wanted to store some parameters
 * in between steps during the job execution 
 */
@Component
@JobScope
public class ExportFileToDbJobExecListener implements JobExecutionListener {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ExportFileToDbJobExecListener.class);
	
	@Value("#{jobParameters['JOB_PARAM_INPUT_FILE_LOC']}")
	private String inputFileLocation;

	@Override
	public void beforeJob(JobExecution jobExecution) {
		MDC.put("LOG_INPUT_FILE_NAME", inputFileLocation);
		LOGGER.info("Start of 'ExportFileToDbBatchJob' Batch Job ");
	}

	// 'afterJob' will be called regardless of the success or
	// failure of the Job. If success or failure needs to be determined it can be
	// obtained from the JobExecution
	@Override
	public void afterJob(JobExecution jobExecution) {
		LOGGER.info("End of 'ExportFileToDbBatchJob' Batch Job ");
		MDC.remove("LOG_INPUT_FILE_NAME");
	}

}
