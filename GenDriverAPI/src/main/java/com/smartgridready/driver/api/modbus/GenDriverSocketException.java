package com.smartgridready.driver.api.modbus;

public class GenDriverSocketException extends Exception {

	private static final long serialVersionUID = 1L;

	public GenDriverSocketException(String aMessage, Throwable aCause) {
		super(aMessage, aCause);
	}

	public GenDriverSocketException(String aMessage) {
		super(aMessage);
	}

	public GenDriverSocketException(Throwable aCause) {
		super(aCause);
	}

}
