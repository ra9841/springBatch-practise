package com.rabin.com.practise.springBatch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabin.com.practise.springBatch.entity.Company;

public class StepSkipListener implements SkipListener<Company,Number>{//t is object,s is number
	
	private static final Logger log = LoggerFactory.getLogger(StepSkipListener.class);

	@Override  //item reader
	public void onSkipInRead(Throwable t) {
		log.info("A failure on read {}",t.getMessage());
		
	}

	@Override  //item writer
	public void onSkipInWrite(Number item, Throwable t) {
		log.info("A failure on write {},{}",t.getMessage(),item);
		
	}

	//@SneakyThrows(in lombok)
	@Override  //item processor
	public void onSkipInProcess(Company item, Throwable t) {
		try {
			log.info("Item {} was skipped due to the exception {}",new ObjectMapper().writeValueAsString(item)+ t.getMessage());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	} 

}
