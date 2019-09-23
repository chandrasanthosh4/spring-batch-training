package com.training.spring.batch.config;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class QuartzJobLauncher extends QuartzJobBean {

	private String jobName;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

		SchedulerContext sc = null;
		try {
			sc = jobExecutionContext.getScheduler().getContext();
		} catch (SchedulerException e) {
			System.err.println("Error:"+ e.getMessage());
		}
		ApplicationContext ctx = (ApplicationContext) sc.get("applicationContextSchedulerContextKey");
		JobLauncher jl = ctx.getBean(JobLauncher.class);
		JobLocator jlo = ctx.getBean(JobLocator.class);
		try {
			Job job = (Job) jlo.getJob(jobName);

			JobParameter jp = new JobParameter(new Date());

			Map<String, JobParameter> parameters = new ConcurrentHashMap<String, JobParameter>();
			parameters.put("dateParam", jp);

			JobParameters jobParameters = new JobParameters(parameters);
			jl.run(job, jobParameters);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException | NoSuchJobException e) {
			e.printStackTrace();
		}

	}

}
