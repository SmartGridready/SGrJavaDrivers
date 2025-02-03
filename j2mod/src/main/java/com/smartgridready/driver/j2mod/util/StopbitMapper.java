package com.smartgridready.driver.j2mod.util;

import java.util.HashMap;
import java.util.Map;

import com.ghgande.j2mod.modbus.net.AbstractSerialConnection;

public class StopbitMapper {

	private static final Map<com.smartgridready.driver.api.modbus.StopBits, Integer> STOPBIT_MAP = new HashMap<>();
	
	static {
		STOPBIT_MAP.put(com.smartgridready.driver.api.modbus.StopBits.ONE, AbstractSerialConnection.ONE_STOP_BIT);
		STOPBIT_MAP.put(com.smartgridready.driver.api.modbus.StopBits.ONE_AND_HALF, AbstractSerialConnection.ONE_POINT_FIVE_STOP_BITS);
		STOPBIT_MAP.put(com.smartgridready.driver.api.modbus.StopBits.TWO, AbstractSerialConnection.TWO_STOP_BITS);
	}
	
	private StopbitMapper() {};
	
	public static Integer map(com.smartgridready.driver.api.modbus.StopBits genStopBit) {
		return STOPBIT_MAP.getOrDefault(genStopBit, AbstractSerialConnection.ONE_STOP_BIT);
	}
}
