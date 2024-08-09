package com.smartgridready.driver.api.modbus;

public class GenDriverModbusException extends Exception {

	private static final long serialVersionUID = 1L;

	public GenDriverModbusException(String aMessage, Throwable aCause) {
		super(aMessage, aCause);
	}

	public GenDriverModbusException(String aMessage) {
		super(aMessage);
	}

	public GenDriverModbusException(Throwable aCause) {
		super(aCause);
	}

}
