package com.training.spring.batch.util;

import java.util.Optional;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.stereotype.Component;

@Component
public class ExportFileToDbJobParameterValidator implements JobParametersValidator {

	@Override
	public void validate(JobParameters parameters) throws JobParametersInvalidException {
		if(Optional.ofNullable(parameters).isPresent() && !parameters.isEmpty()) {
			if(!parameters.getParameters().containsKey("JOB_PARAM_DATE")) {
				throw new JobParametersInvalidException("Job parameter 'JOB_PARAM_DATE' is missing. Exiting the Job !!");
			} else if(!parameters.getParameters().containsKey("JOB_PARAM_INPUT_FOLDER_LOC")) {
				throw new JobParametersInvalidException("Job parameter 'JOB_PARAM_INPUT_FOLDER_LOC' is missing. Exiting the Job !!");
			}
		}
	}
}
