package com.training.spring.batch.job;

import java.io.File;
import java.util.Optional;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@JobScope
public class ExportFileToDbBatchJobExecDecider implements JobExecutionDecider {

	@Value("#{jobExecutionContext['INPUT_FILE']}")
	private String inputFileLocation;

	@Override
	public FlowExecutionStatus decide(JobExecution jobExecution, StepExecution stepExecution) {
		if (stepExecution.getExitStatus().getExitCode() == "COMPLETED"
				&& Optional.ofNullable(inputFileLocation).isPresent()) {
			File inputFile = new File(inputFileLocation);
			if (inputFile.exists() && inputFile.isFile()) {
				return FlowExecutionStatus.COMPLETED;
			}
		}
		return FlowExecutionStatus.FAILED;
	}

}
