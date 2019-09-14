package com.training.spring.batch.job;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.validation.BindException;

import com.training.spring.batch.dao.CustomerDao;
import com.training.spring.batch.entity.Customer;

@Configuration
public class ExportFileToDbBatchJobConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private CustomerDao customerDao;

	/**
	 * Job Configuration
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean("ExportFileToDbJob")
	public Job dbToFileJobConfiguration() throws Exception {
		return jobBuilderFactory.get("ExportFileToDbJob").start(fileToDbStepConfiguration()).build();
	}

	/**
	 * Step Configuration with the chunk(no of records to process in bulk at once)
	 * value 1
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean
	public Step fileToDbStepConfiguration() throws Exception {
		return stepBuilderFactory.get("ExportFileToDbStep").<Customer, Customer>chunk(2).reader(fileRecordReader())
				.writer(dbRecordWriter()).build();
	}

	@Bean
	public ItemReader<Customer> fileRecordReader() throws Exception {
		FlatFileItemReader<Customer> r = new FlatFileItemReader<Customer>();
		r.setResource(new FileSystemResource("C://Users//sapenumarthi//batchjobs//customer_db_to_file.txt"));
		DefaultLineMapper<Customer> lm = new DefaultLineMapper<Customer>();
		DelimitedLineTokenizer dlt = new DelimitedLineTokenizer();
		dlt.setDelimiter("|");
		dlt.setNames(new String[] { "customerId", "customerActiveInd", "customerAddress", "customerEmail",
				"customerMobile", "customerName", "customerVersion" });
		lm.setLineTokenizer(dlt);
		FieldSetMapper<Customer> fsm = new FieldSetMapper<Customer>() {
			@Override
			public Customer mapFieldSet(FieldSet fs) throws BindException {
				Customer c = new Customer();
				c.setCustomerActiveInd(fs.readBoolean("customerActiveInd"));
				c.setCustomerAddress(fs.readString("customerAddress"));
				c.setCustomerEmail(fs.readString("customerEmail"));
				c.setCustomerMobile(fs.readString("customerMobile"));
				c.setCustomerName(fs.readString("customerName"));
				return c;
			}
		};
		lm.setFieldSetMapper(fsm);
		r.setLineMapper(lm);
		r.afterPropertiesSet();
		return r;
	}

	@Bean
	public ItemWriter<Customer> dbRecordWriter() {
		return new ItemWriter<Customer>() {
			@Override
			public void write(List<? extends Customer> cs) throws Exception {
				customerDao.save(cs);
			}
		};
	}

}
