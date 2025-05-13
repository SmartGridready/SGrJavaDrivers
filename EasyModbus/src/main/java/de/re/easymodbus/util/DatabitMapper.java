package de.re.easymodbus.util;

import java.util.HashMap;
import java.util.Map;

import de.re.easymodbus.datatypes.DataBits;

/**
 * Maps the {@link com.smartgridready.driver.api.modbus.DataBits} enumerations to EasyModbus equivalent.
 */
public class DatabitMapper {
	private static final Map<com.smartgridready.driver.api.modbus.DataBits, DataBits> DATABIT_MAP = new HashMap<>();
	
	static {
		DATABIT_MAP.put(com.smartgridready.driver.api.modbus.DataBits.SEVEN, DataBits.Seven);
		DATABIT_MAP.put(com.smartgridready.driver.api.modbus.DataBits.EIGHT, DataBits.Eight);
	}
	
	private DatabitMapper() {};
	
	/**
	 * Maps data bits value.
	 * @param genDataBit the data bits value
	 * @return an instance of {@link DataBits}
	 */
	public static DataBits map(com.smartgridready.driver.api.modbus.DataBits genDataBit) {
		return DATABIT_MAP.getOrDefault(genDataBit, DataBits.Eight);
	}
}
