package com.smartgridready.driver.modbus.api;

public class GenDriverException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public GenDriverException( String aMessage) {
		super (aMessage);
	}
	
	public GenDriverException( Throwable aCause) {
		super (aCause);
	}
	
	public GenDriverException( String aMessage, Throwable aCause) {
		super(aMessage, aCause);
	}	
}
