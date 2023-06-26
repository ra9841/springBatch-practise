package com.rabin.com.practise.springBatch.config;

import org.springframework.batch.core.step.skip.SkipLimitExceededException;
import org.springframework.batch.core.step.skip.SkipPolicy;

public class ExceptionSkipPolicy implements SkipPolicy{

	@Override
	public boolean shouldSkip(Throwable t, int skipCount) throws SkipLimitExceededException {
		
		return t instanceof Exception;//in place of Exception put if the exception is NumberFormatException.
		//we can add other exception and logic here
	}

}
