package com.hisoft.hscloud.common.exception;

public class NoUniqueException extends RuntimeException {
	
	public NoUniqueException() {
		super();
	}


	public NoUniqueException(String message) {
		super("Result is not Unique.");
	}

}
