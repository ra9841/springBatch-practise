package com.rabin.com.practise.springBatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.rabin.com.practise.springBatch.entity.Company;
import com.rabin.com.practise.springBatch.listener.StepSkipListener;
import com.rabin.com.practise.springBatch.repository.CompanyRepository;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
	
	@Autowired
	private JobBuilderFactory jobBuilderFactory;
	
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	
	public FlatFileItemReader<Company> reader(){
		FlatFileItemReader<Company> itemReader=new FlatFileItemReader<>();
		itemReader.setResource(new FileSystemResource("src/main/resources/csvfile.csv"));//path
		itemReader.setName("csvReader");
		itemReader.setLinesToSkip(1);//it will skip first line
		itemReader.setLineMapper(lineMapper());//creating lineMapper method
		
		return itemReader;
	}


	private LineMapper<Company> lineMapper() {//extract and mapped to company object.how to read csv file to company object
		DefaultLineMapper<Company> lineMapper=new DefaultLineMapper<>();
		
		DelimitedLineTokenizer delimitedLineTokenizer=new DelimitedLineTokenizer();//extract the value from csv file
		delimitedLineTokenizer.setDelimiter(",");//delimitedLineTokenizer will read coma seperated value
		delimitedLineTokenizer.setStrict(false);
		delimitedLineTokenizer.setNames("name","email","password","address","username","gender","age");
		
		BeanWrapperFieldSetMapper<Company>fieldSetMapper=new BeanWrapperFieldSetMapper<>();//mapped csv file to company object
		fieldSetMapper.setTargetType(Company.class);
		
		lineMapper.setLineTokenizer(delimitedLineTokenizer);
		lineMapper.setFieldSetMapper(fieldSetMapper);
		
		return lineMapper;
	}
	@Bean
	public CompanyProcessor processor() {
		return new CompanyProcessor();
	}
	
	@Bean
	public RepositoryItemWriter<Company>writer(){
		RepositoryItemWriter<Company> writer=new RepositoryItemWriter<>();
		writer.setRepository(companyRepository);
		writer.setMethodName("save");
		//telling in writer that just used my companyRepository.save method to write the information/csv data to database
		
		return writer;
	}
	
	@Bean
	public Step step1() {
		return stepBuilderFactory.get("csv-step").<Company,Company>chunk(30)//we can give any name.Inside chunk how many data you want to present
				.reader(reader())
				.processor(processor())
				.writer(writer())
				.faultTolerant()
				//.skipLimit(50)
				//.skip(Exception.class)//explain if any kind of exception is happening
				//.noSkip(IllegalArgumentException.class)//no skip if any illegal format exception occured
				//.taskExecutor(taskExecutor())
				.listener(skipListener())
				.skipPolicy(skipPolicy())
				.build();
		//As the flow of diagram: giving reader,processor and writer object to step
		
	}
	@Bean
	public Job runJob() {
		return jobBuilderFactory.get("importCompany")//we can give any name
				.flow(step1())
			//	.next(step2())  if you have more than one step
				.end().build();
		
	}
//	@Bean
//	public TaskExecutor taskExecutor() {
//		SimpleAsyncTaskExecutor asyncTaskExecutor=new SimpleAsyncTaskExecutor();
//		asyncTaskExecutor.setConcurrencyLimit(10);
//		return asyncTaskExecutor;
//		By providing the TaskExecutor bean to the taskExecutor() method, you enable parallel execution of 
	//tasks within the batch step.
//	}
	
	@Bean
	public SkipPolicy skipPolicy() {
		return new ExceptionSkipPolicy();
	}
	
	@Bean
	public SkipListener skipListener() {
		return new StepSkipListener();
	}

}
