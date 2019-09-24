package com.training.spring.batch.util;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.stereotype.Component;

@Component
public class ExportFileToDbJobParameterValidator implements JobParametersValidator {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(ExportFileToDbJobParameterValidator.class);

	@Override
	public void validate(JobParameters parameters) throws JobParametersInvalidException {
		LOGGER.info("Start of validate method");
		if(Optional.ofNullable(parameters).isPresent() && !parameters.isEmpty()) {
			if(!parameters.getParameters().containsKey("JOB_PARAM_DATE")) {
				LOGGER.error("Job parameter 'JOB_PARAM_DATE' is missing. Exiting the Job !!");
				throw new JobParametersInvalidException("Job parameter 'JOB_PARAM_DATE' is missing. Exiting the Job !!");
			} else if(!parameters.getParameters().containsKey("JOB_PARAM_INPUT_FILE_LOC")) {
				LOGGER.error("Job parameter 'JOB_PARAM_INPUT_FILE_LOC' is missing. Exiting the Job !!");
				throw new JobParametersInvalidException("Job parameter 'JOB_PARAM_INPUT_FILE_LOC' is missing. Exiting the Job !!");
			}
		} else {
			LOGGER.error("Job parameters are missing. Exiting the Job !!");
			throw new JobParametersInvalidException("Job parameters are missing. Exiting the Job !!");
		}
		LOGGER.info("End of validate method");
	}

}
