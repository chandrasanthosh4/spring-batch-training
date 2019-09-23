package com.training.spring.batch.config;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(basePackages="com.training.spring.batch")
@EntityScan(basePackages="com.training.spring.batch.entity")
@EnableJpaRepositories(basePackages="com.training.spring.batch.dao")
@EnableTransactionManagement(proxyTargetClass=true)
@EnableBatchProcessing
public class BatchConfig {

}
