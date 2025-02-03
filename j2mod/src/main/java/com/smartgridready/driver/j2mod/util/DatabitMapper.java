package com.smartgridready.driver.j2mod.util;

import java.util.HashMap;
import java.util.Map;

public class DatabitMapper {

	private static final Map<com.smartgridready.driver.api.modbus.DataBits, Integer> DATABIT_MAP = new HashMap<>();
	
	static {
		DATABIT_MAP.put(com.smartgridready.driver.api.modbus.DataBits.SEVEN, 7);
		DATABIT_MAP.put(com.smartgridready.driver.api.modbus.DataBits.EIGHT, 8);
	}
	
	private DatabitMapper() {};
	
	public static Integer map(com.smartgridready.driver.api.modbus.DataBits genDataBit) {
		return DATABIT_MAP.getOrDefault(genDataBit, 8);
	}
}
