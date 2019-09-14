package com.training.spring.batch.config;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@SpringBootConfiguration
public class QuartzConfig {

	@Autowired
	private ApplicationContext applicationContext;

	@Bean
	public JobDetailFactoryBean jobDetailFactoryBean() {
		JobDetailFactoryBean jd = new JobDetailFactoryBean();
		jd.setJobClass(QuartzJobLauncher.class);
		jd.setBeanName("ExportDbToFileJob");
		return jd;
	}

	@Bean
	public CronTriggerFactoryBean cronTriggerFactoryBean() {
		CronTriggerFactoryBean ctf = new CronTriggerFactoryBean();
		ctf.setJobDetail(jobDetailFactoryBean().getObject());
		ctf.setCronExpression("0 0/2 * 1/1 * ? *");
		ctf.setName("ExportDbToFileJob_trigger");
		return ctf;
	}

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		SchedulerFactoryBean sf = new SchedulerFactoryBean();

		// set JobFactory
		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		sf.setJobFactory(jobFactory);

		// set jobNames to schedulerContextMap
		Map<String, Object> schedulerContextAsMap = new ConcurrentHashMap<String, Object>();
		schedulerContextAsMap.put("jobName", "ExportDbToFileJob");
		sf.setSchedulerContextAsMap(schedulerContextAsMap);

		// set ApplicationContext to be retrieved using the below mentioned key
		sf.setApplicationContext(applicationContext);
		sf.setApplicationContextSchedulerContextKey("applicationContextSchedulerContextKey");

		// load and set quartz.properties
		sf.setQuartzProperties(quartzProperties());

		// finally set the crontrigger details
		sf.setTriggers(cronTriggerFactoryBean().getObject());
		return sf;
	}

	@Bean
	public Properties quartzProperties() {
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
		Properties properties = null;
		try {
			propertiesFactoryBean.afterPropertiesSet();
			properties = propertiesFactoryBean.getObject();
		} catch (IOException e) {
		}
		return properties;
	}

}
