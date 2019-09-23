package com.training.spring.batch.job;

import java.io.File;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

@Component
@StepScope
public class ExportFileToDbFileArchivalTasklet implements Tasklet {
	
	@Value("#{jobParameters['JOB_PARAM_INPUT_FILE_LOC']}")
	private String inputFileLocation;
	
	@Value("#{jobParameters['JOB_PARAM_FILE_ARCHIVAL_LOC']}")
	private String fileArchivalLocation;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		File inFile = new File(inputFileLocation);
		inFile.getName();
		File outFile = new File(fileArchivalLocation+inFile.getName()+"_"+System.currentTimeMillis());
		if (!outFile.exists()) {
			outFile.createNewFile();
		}
		FileCopyUtils.copy(inFile, outFile);
		return RepeatStatus.FINISHED;
	}

}
