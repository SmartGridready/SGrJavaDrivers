package de.re.easymodbus.util;

import java.util.HashMap;
import java.util.Map;

import de.re.easymodbus.datatypes.StopBits;

/**
 * Maps the {@code com.smartgridready.driver.api.modbus.StopBits} enumerations to EasyModbus equivalent.
 */
public class StopbitMapper {
	private static final Map<com.smartgridready.driver.api.modbus.StopBits, StopBits> STOPBIT_MAP = new HashMap<>();
	
	static {
		STOPBIT_MAP.put(com.smartgridready.driver.api.modbus.StopBits.ONE, StopBits.One);
		STOPBIT_MAP.put(com.smartgridready.driver.api.modbus.StopBits.ONE_AND_HALF, StopBits.OnePointFive);
		STOPBIT_MAP.put(com.smartgridready.driver.api.modbus.StopBits.TWO, StopBits.Two);
	}
	
	private StopbitMapper() {};
	
	/**
	 * Maps stop bits value.
	 * @param genStopBit the stop bits value
	 * @return an instance of {@code StopBits}
	 */
	public static StopBits map(com.smartgridready.driver.api.modbus.StopBits genStopBit) {
		return STOPBIT_MAP.getOrDefault(genStopBit, StopBits.One);
	}
}
