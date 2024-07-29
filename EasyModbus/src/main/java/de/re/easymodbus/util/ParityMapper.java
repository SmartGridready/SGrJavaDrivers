package de.re.easymodbus.util;

import java.util.HashMap;
import java.util.Map;

import de.re.easymodbus.datatypes.Parity;

public class ParityMapper {
	
	private static final Map<com.smartgridready.driver.modbus.api.Parity, Parity> PARITY_MAP = new HashMap<>();
	
	static {
		PARITY_MAP.put(com.smartgridready.driver.modbus.api.Parity.NONE, Parity.None);
		PARITY_MAP.put(com.smartgridready.driver.modbus.api.Parity.EVEN, Parity.Even);
		PARITY_MAP.put(com.smartgridready.driver.modbus.api.Parity.ODD, Parity.Odd);
	}
	
	private ParityMapper() {};
	
	public static Parity map(com.smartgridready.driver.modbus.api.Parity genParity) {
		return PARITY_MAP.getOrDefault(genParity, Parity.None);		
	}

}
