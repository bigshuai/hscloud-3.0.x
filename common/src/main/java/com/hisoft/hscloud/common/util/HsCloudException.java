package com.hisoft.hscloud.common.util;

import org.apache.log4j.Logger;




public class HsCloudException extends RuntimeException{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -149395889164377020L;

	private Logger logger = Logger.getLogger(HsCloudException.class);
	
	private String code;
	
	public HsCloudException() {}
	/**
	 * 后台打印code并向上传递code
	 * @param code
	 * @param logger
	 */
	public HsCloudException(String code, Logger logger) {
		super(code);
		this.code = code;
		logger.error(code, this);
	}
	/**
	 * 后台打印message，向上传递code
	 * @param code
	 * @param message
	 * @param logger
	 */
	public HsCloudException(String code,String message, Logger logger) {
		super(message);
		this.code = code;
		logger.error(message, this);
	}
	
	public HsCloudException(String message, Exception ex) {
		super(message, ex);
		logger.error(message, ex);
		logger.error(ex);
	}
	
	public HsCloudException(String code, Logger logger, Exception ex) {
		super(code, ex);
		if(!(ex instanceof  HsCloudException)){
			this.code = code;
			logger.error(code, ex);
			logger.error(ex);
		} else {
			this.code = ((HsCloudException)ex).getCode();
		}
	}
	
	public HsCloudException(String code, String message, Logger logger, Exception ex) {
		super(message, ex);
		if(!(ex instanceof  HsCloudException)){
			this.code = code;
			logger.error(message, ex);
			logger.error(ex);
		} else {
			this.code = ((HsCloudException)ex).getCode();
		}
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
