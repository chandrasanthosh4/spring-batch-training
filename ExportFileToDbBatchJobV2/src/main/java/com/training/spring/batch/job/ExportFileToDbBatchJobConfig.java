package com.training.spring.batch.job;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.validation.BindException;

import com.training.spring.batch.dao.CustomerDao;
import com.training.spring.batch.entity.Customer;
import com.training.spring.batch.exception.ExportFileToDbJobBusinessException;
import com.training.spring.batch.listener.CustomerEntityWriterListener;
import com.training.spring.batch.listener.CustomerRecordProcessorListener;
import com.training.spring.batch.listener.ExportFileToDbChunkListener;
import com.training.spring.batch.listener.ExportFileToDbJobExecListener;
import com.training.spring.batch.listener.ExportFileToDbSkipListener;
import com.training.spring.batch.listener.ExportFileToDbStepExecListener;
import com.training.spring.batch.listener.FlatFileItemReaderListener;
import com.training.spring.batch.pojo.CustomerRecord;
import com.training.spring.batch.util.CustomerRecordProcessor;
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
	private ExportFileToDbJobExecListener exportFileToDbJobExecListener; 
	
	@Autowired
	private ExportFileToDbStepExecListener exportFileToDbStepExecListener;
	
	@Autowired
	private ExportFileToDbChunkListener exportFileToDbChunkListener;
	
	@Autowired
	private FlatFileItemReaderListener flatFileItemReaderListener;
	
	@Autowired
	private CustomerRecordProcessorListener customerRecordProcessorListener;
	
	@Autowired
	private CustomerEntityWriterListener customerEntityWriterListener;
	
	@Autowired
	private ExportFileToDbSkipListener exportFileToDbSkipListener;
	
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
				.listener(exportFileToDbJobExecListener)
				.validator(exportFileToDbJobParameterValidator)
				.start(fileToDbStepConfiguration())
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
		return stepBuilderFactory.get("ExportFileToDbStep")
				.<CustomerRecord, Customer>chunk(2)
				.reader(fileRecordReader(null))
				.listener(flatFileItemReaderListener)
				.processor(customerRecordProcessor)
				.listener(customerRecordProcessorListener)
				.writer(dbRecordWriter())
				.listener(customerEntityWriterListener)
				.faultTolerant()
				.skipLimit(3)
				.skip(ExportFileToDbJobBusinessException.class)
				.allowStartIfComplete(false)
				.listener(exportFileToDbSkipListener)
				.listener(exportFileToDbChunkListener)
				.listener(exportFileToDbStepExecListener)
				.build();
	}

	// Use FlatFileItemReader rather than ItemReader to avoid the below error
	// 'org.springframework.batch.item.ReaderNotOpenException: Reader
	// must be open before it can be read'
	@Bean
	@StepScope
	public FlatFileItemReader<CustomerRecord> fileRecordReader(
			@Value("#{jobParameters['JOB_PARAM_INPUT_FILE_LOC']}") String inputFileLocation) throws Exception {
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
