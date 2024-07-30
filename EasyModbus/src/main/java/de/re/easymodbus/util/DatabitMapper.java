package de.re.easymodbus.util;

import java.util.HashMap;
import java.util.Map;

import de.re.easymodbus.datatypes.DataBits;


public class DatabitMapper {
	private static final Map<com.smartgridready.driver.api.modbus.DataBits, DataBits> DATABIT_MAP = new HashMap<>();
	
	static {
		DATABIT_MAP.put(com.smartgridready.driver.api.modbus.DataBits.SEVEN, DataBits.Seven);
		DATABIT_MAP.put(com.smartgridready.driver.api.modbus.DataBits.EIGHT, DataBits.Eight);
	}
	
	private DatabitMapper() {};
	
	public static DataBits map(com.smartgridready.driver.api.modbus.DataBits genDataBit) {
		return DATABIT_MAP.getOrDefault(genDataBit, DataBits.Eight);
	}
}
