package com.training.spring.batch.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class ExportFileToDbStepExecListener implements StepExecutionListener {

	@Override
	public void beforeStep(StepExecution stepExecution) {
		System.out.println("Start of 'ExportFileToDbBatchStep'");
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		System.out.println("End of 'ExportFileToDbBatchStep'");
		return stepExecution.getExitStatus();
	}

}
