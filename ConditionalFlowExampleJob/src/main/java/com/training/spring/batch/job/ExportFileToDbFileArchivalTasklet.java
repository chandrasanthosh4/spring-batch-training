package com.training.spring.batch.job;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.training.spring.batch.exception.FileArchivalBusinessException;

@Component
@JobScope
public class ExportFileToDbFileArchivalTasklet implements Tasklet {

	@Value("#{jobParameters['JOB_PARAM_INPUT_FILE_LOC']}")
	private String inputFileLocation;

	@Value("#{jobParameters['JOB_PARAM_FILE_ARCHIVAL_LOC']}")
	private String fileArchivalLocation;

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		File inFile = new File(inputFileLocation);
		String[] fileNameSplits = inFile.getName().split("\\.");
		String archivalLocation = fileArchivalLocation + fileNameSplits[0] + "_" + System.currentTimeMillis() + "."
				+ fileNameSplits[1];
		// path is an object that may be used to locate a file in a file system
		Path path = Files.move(Paths.get(inputFileLocation), Paths.get(archivalLocation));
		if (Optional.ofNullable(path).isPresent()) {
			return RepeatStatus.FINISHED;
		}
		throw new FileArchivalBusinessException("Unable to archive the file");
	}

}
