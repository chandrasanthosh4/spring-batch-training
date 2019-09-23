package com.training.spring.batch.app;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.training.spring.batch.config.BatchConfig;

@SpringBootApplication(scanBasePackageClasses = BatchConfig.class)
public class ExportFileToDbBatchJobApp {

	public static void main(String[] args) {
		
		ApplicationContext context = SpringApplication.run(ExportFileToDbBatchJobApp.class, args);
		
		//JobLauncher run the actual batch job, during the run all the
		//job levels details are persisted to database through JobRepository(Implicitly)
		JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
		Job job = (Job) (context.getBean("ExportFileToDbJob"));

		try {
			//JobParameters are used to distinguish JobInstances from one another
			//If we pass the same JobParameters again to run the completed job,
			//job runs and verifies in the batch metadata tables to see if there 
			//was any job instance(with the same job parameters) thats already 
			//completed and if it come to know "yes", then it returns "COMPLETED" status
			JobParameter currentDateJobParam = new JobParameter(new Date());
			JobParameter inputFileLocationJobParam = new JobParameter("C://Users//sapenumarthi//batchjobs//customer.txt");
			
			Map<String, JobParameter> parameters = new ConcurrentHashMap<String, JobParameter>();
			parameters.put("JOB_PARAM_DATE", currentDateJobParam);
			parameters.put("JOB_PARAM_INPUT_FILE_LOC", inputFileLocationJobParam);
			
			JobParameters jobParameters = new JobParameters(parameters);
			JobExecution execution = jobLauncher.run(job, jobParameters);
			System.out.println("Batch Status : " + execution.getStatus());
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		System.out.println("Done");
	}

}
