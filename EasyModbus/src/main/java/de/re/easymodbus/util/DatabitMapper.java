package de.re.easymodbus.util;

import java.util.HashMap;
import java.util.Map;

import de.re.easymodbus.datatypes.DataBits;


public class DatabitMapper {
	private static final Map<communicator.common.runtime.DataBits, DataBits> DATABIT_MAP = new HashMap<>();
	
	static {
		DATABIT_MAP.put(communicator.common.runtime.DataBits.SEVEN, DataBits.Seven);
		DATABIT_MAP.put(communicator.common.runtime.DataBits.EIGHT, DataBits.Eight);
	}
	
	private DatabitMapper() {};
	
	public static DataBits map(communicator.common.runtime.DataBits genDataBit) {
		return DATABIT_MAP.getOrDefault(genDataBit, DataBits.Eight);
	}
}
