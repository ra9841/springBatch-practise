package com.rabin.com.practise.springBatch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.rabin.com.practise.springBatch.entity.Company;

public class CompanyProcessor implements ItemProcessor<Company,Company>{//read input object as Company and write it as Company(inbound and outbound)

	private static final Logger log = LoggerFactory.getLogger(CompanyProcessor.class);
	

	@Override
	public Company process(Company item) throws Exception {
		log.info("List of item:---"+ item);
		//we can put logic for data which are needed(for filtering data)
			return item;
		}
	
	//we can put logic for data which are needed(for filtering data)
//	@Override
//	public Company process(Company item) throws Exception {
//		log.info("List of item:---"+ item);
//		if(item.getGender().equals("Male")) {
//			return item;
//		}else {
//		return null;
//		}
//	}
	

}
