package com.training.spring.batch.listener;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

/**
 * JobExection is usually needed if we wanted to store some parameters
 * in between steps during the job execution 
 */
@Component
public class ExportFileToDbJobExecListener implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution jobExecution) {
		System.out.println("Start of 'ExportFileToDbBatchJob' Batch Job ");
	}

	// 'afterJob' will be called regardless of the success or
	// failure of the Job. If success or failure needs to be determined it can be
	// obtained from the JobExecution
	@Override
	public void afterJob(JobExecution jobExecution) {
		System.out.println("End of 'ExportFileToDbBatchJob' Batch Job ");
	}

}
