package com.pactera.hscloud.openstackhandler.exception;

import com.pactera.hscloud.common4j.exception.HandlerException;

/**
 * 
 * @author Minggang
 *
 */
@SuppressWarnings("serial")
public class OpenstackHandlerException extends HandlerException {

	public OpenstackHandlerException() {
		super();
	}

	public OpenstackHandlerException(String message, Throwable cause) {
		super(message, cause);
	}

	public OpenstackHandlerException(String message) {
		super(message);
	}

	public OpenstackHandlerException(Throwable cause) {
		super(cause);
	}
	

}
