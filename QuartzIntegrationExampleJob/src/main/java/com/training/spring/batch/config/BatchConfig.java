package com.training.spring.batch.config;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootConfiguration
@EnableBatchProcessing
@EnableAutoConfiguration
@ComponentScan(basePackages="com.training.spring.batch")
@EntityScan(basePackages="com.training.spring.batch.entity")
@EnableJpaRepositories(basePackages="com.training.spring.batch.dao")
@EnableTransactionManagement(proxyTargetClass=true)
@Import(QuartzConfig.class)
public class BatchConfig {

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	
	@Autowired
	private JobRegistry jobRegistry;
	
	@Bean
	public JobRepository jobRepository() throws Exception {
		JobRepositoryFactoryBean jr = new JobRepositoryFactoryBean();
		jr.setDataSource(dataSource);
		jr.setTransactionManager(transactionManager);
		jr.afterPropertiesSet();
		return jr.getObject();
	}
	
	@Bean
	public TaskExecutor taskExecutor() {
		return new SimpleAsyncTaskExecutor();
	}
	
	@Bean
	public JobLauncher jobLauncher() throws Exception {
		SimpleJobLauncher jl = new SimpleJobLauncher();
		jl.setJobRepository(jobRepository());
		jl.setTaskExecutor(taskExecutor());
		jl.afterPropertiesSet();
		return jl;
	}
	
	@Bean
	public JobExplorer jobExplorer() throws Exception {
		JobExplorerFactoryBean je = new JobExplorerFactoryBean();
		je.setDataSource(dataSource);
		je.afterPropertiesSet();
		return je.getObject();
	}
	
	@Bean
	public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor() throws Exception {
		JobRegistryBeanPostProcessor jr = new JobRegistryBeanPostProcessor();
		jr.setJobRegistry(jobRegistry);
		jr.afterPropertiesSet();
		return jr;
	}
	
	@Bean
	public SimpleJobOperator jobOperator() throws Exception {
		SimpleJobOperator jb = new SimpleJobOperator();
		jb.setJobRepository(jobRepository());
		jb.setJobLauncher(jobLauncher());
		jb.setJobExplorer(jobExplorer());
		jb.setJobRegistry(jobRegistry);
		jb.afterPropertiesSet();
		return jb;
	}
}
