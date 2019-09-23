package com.training.spring.batch.job;

import java.util.List;

import javax.persistence.OptimisticLockException;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.validation.BindException;

import com.training.spring.batch.dao.CustomerDao;
import com.training.spring.batch.entity.Customer;
import com.training.spring.batch.exception.ValidationBusinessException;
import com.training.spring.batch.pojo.CustomerRecord;
import com.training.spring.batch.util.ExportFileToDbJobParameterValidator;

@Configuration
public class ExportFileToDbBatchJobConfig {

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private ExportFileToDbJobParameterValidator exportFileToDbJobParameterValidator;

	@Autowired
	private CustomerRecordProcessor customerRecordProcessor;

	@Autowired
	private ExportFileToDbBatchJobExecDecider exportFileToDbBatchJobExecDecider;
	
	@Autowired
	private ExportFileToDbFileProviderTasklet exportFileToDbFileProviderTasklet;
	
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
		return jobBuilderFactory.get("ExportFileToDbJob")
				.validator(exportFileToDbJobParameterValidator)
				.start(fileProviderTasklet())
				.next(exportFileToDbBatchJobExecDecider).on("COMPLETED").to(fileToDbStepConfiguration())
				.from(exportFileToDbBatchJobExecDecider).on("FAILED").end().build()
				.build();
	}
	
	@Bean
	public Step fileProviderTasklet() {
		return stepBuilderFactory.get("FileProviderTaskletStep")
				.tasklet(exportFileToDbFileProviderTasklet)
				.allowStartIfComplete(false)
				.build();
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
		DefaultTransactionAttribute ta = new DefaultTransactionAttribute();
		ta.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		ta.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
		
		return stepBuilderFactory.get("ExportFileToDbStep")
				.<CustomerRecord, Customer>chunk(2)
				.reader(fileRecordReader(null))
				.processor(customerRecordProcessor)
				.writer(dbRecordWriter())
				.faultTolerant()
				.skip(FlatFileParseException.class)
				.skip(NumberFormatException.class)
				.skip(ValidationBusinessException.class)
				.skipLimit(10)
				.retry(OptimisticLockException.class)
				.retryLimit(3)
				.transactionAttribute(ta)
				.allowStartIfComplete(false)
				.build();
	}

	// Use FlatFileItemReader rather than ItemReader to avoid the below error
	// 'org.springframework.batch.item.ReaderNotOpenException: Reader
	// must be open before it can be read'
	@Bean
	@StepScope
	public FlatFileItemReader<CustomerRecord> fileRecordReader(
			@Value("#{jobExecutionContext['INPUT_FILE']}") String inputFileLocation) throws Exception {
		FlatFileItemReader<CustomerRecord> r = new FlatFileItemReader<CustomerRecord>();
		r.setResource(new FileSystemResource(inputFileLocation));
		DefaultLineMapper<CustomerRecord> lm = new DefaultLineMapper<CustomerRecord>();
		DelimitedLineTokenizer dlt = new DelimitedLineTokenizer();
		dlt.setDelimiter("|");
		dlt.setNames(new String[] { "customerId", "customerActiveInd", "customerAddress", "customerEmail",
				"customerMobile", "customerName", "customerVersion" });
		lm.setLineTokenizer(dlt);
		FieldSetMapper<CustomerRecord> fsm = new FieldSetMapper<CustomerRecord>() {
			@Override
			public CustomerRecord mapFieldSet(FieldSet fs) throws BindException {
				CustomerRecord c = new CustomerRecord();
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
