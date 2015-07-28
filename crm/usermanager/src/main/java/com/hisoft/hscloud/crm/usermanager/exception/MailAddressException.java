package com.hisoft.hscloud.crm.usermanager.exception;

public class MailAddressException extends Exception {
	
	public MailAddressException() {
		super();
	}


	public MailAddressException(String message) {
		super("Mail Address is not exist");
	}


}
