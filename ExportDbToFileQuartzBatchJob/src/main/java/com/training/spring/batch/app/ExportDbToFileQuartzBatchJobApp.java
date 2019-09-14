package com.training.spring.batch.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.training.spring.batch.config.BatchConfig;

@SpringBootApplication(scanBasePackageClasses = BatchConfig.class)
public class ExportDbToFileQuartzBatchJobApp {

	public static void main(String[] args) {
		SpringApplication.run(ExportDbToFileQuartzBatchJobApp.class, args);
	}

}
