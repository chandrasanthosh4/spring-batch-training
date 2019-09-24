package com.training.spring.batch.job;

import java.io.File;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.training.spring.batch.exception.CurruptFolderBusinessException;

@Component
@JobScope
public class ExportFileToDbFileProviderTasklet implements Tasklet {

	@Value("#{jobParameters['JOB_PARAM_INPUT_FOLDER_LOC']}")
	private String inputFolderLocation;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		File inFolder = new File(inputFolderLocation);
		if(inFolder.exists() && inFolder.isDirectory()) {
			if(inFolder.list().length > 0) {
				String[] fileNameArray = inFolder.list();
				String inputfileName = inputFolderLocation+fileNameArray[0];
				chunkContext.getStepContext().getStepExecution()
				.getJobExecution().getExecutionContext().put("INPUT_FILE",inputfileName);
			}
			return RepeatStatus.FINISHED;
		}
		throw new CurruptFolderBusinessException("Input folder doesn't exist or not a directory");
	}

}
