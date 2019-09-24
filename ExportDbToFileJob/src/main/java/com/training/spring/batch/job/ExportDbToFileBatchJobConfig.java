package com.training.spring.batch.job;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.RowMapper;

import com.training.spring.batch.entity.Customer;

@Configuration
public class ExportDbToFileBatchJobConfig {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private DataSource dataSource;

	/**
	 * Job Configuration
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean("ExportDbToFileJob")
	public Job dbToFileJobConfiguration() throws Exception {
		return jobBuilderFactory.get("ExportDbToFileJob").start(dbToFileStepConfiguration()).build();
	}

	/**
	 * Step Configuration with the chunk(no of records to process in bulk at once)
	 * value 1
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean
	public Step dbToFileStepConfiguration() throws Exception {
		return stepBuilderFactory.get("ExportDbToFileStep").<Customer, Customer>chunk(1).reader(dbRecordReader())
				.writer(fileRecordWriter(null)).build();
	}

	/**
	 * ItemReader Configuration to read records from DB
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean
	public JdbcCursorItemReader<Customer> dbRecordReader() throws Exception {
		JdbcCursorItemReader<Customer> ir = new JdbcCursorItemReader<Customer>();
		ir.setDataSource(dataSource);
		// "batch" is a database in MySqsl DB
		ir.setSql("SELECT * FROM batch.customer");
		ir.setRowMapper(new RowMapper<Customer>() {
			@Override
			public Customer mapRow(ResultSet rs, int rowNo) throws SQLException {
				Customer c = new Customer();
				c.setCustomerId(rs.getLong(1));
				c.setCustomerActiveInd(rs.getBoolean(2));
				c.setCustomerAddress(rs.getString(3));
				c.setCustomerEmail(rs.getString(4));
				c.setCustomerMobile(rs.getString(5));
				c.setCustomerName(rs.getString(6));
				c.setCustomerVersion(rs.getLong(7));
				return c;
			}
		});
		return ir;
	}

	/**
	 * ItemWriter Configuration to write the records that are read from ItemReader
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean
	@StepScope
	public FlatFileItemWriter<Customer> fileRecordWriter(
			@Value("#{jobParameters['JOB_PARAM_INPUT_FILE_LOC']}") String inputFileLocation) throws Exception {
		FlatFileItemWriter<Customer> iw = new FlatFileItemWriter<Customer>();
		// The complete file path to write the data
		iw.setResource(new FileSystemResource(inputFileLocation));
		iw.setShouldDeleteIfExists(true);
		iw.setAppendAllowed(true);
		DelimitedLineAggregator<Customer> dla = new DelimitedLineAggregator<Customer>();
		dla.setDelimiter("|");
		BeanWrapperFieldExtractor<Customer> fe = new BeanWrapperFieldExtractor<Customer>();
		// The property names from the Customer Entity
		fe.setNames(new String[] { "customerId", "customerActiveInd", "customerAddress", "customerEmail",
				"customerMobile", "customerName", "customerVersion" });
		dla.setFieldExtractor(fe);
		iw.setLineAggregator(dla);
		iw.afterPropertiesSet();
		return iw;
	}

}
