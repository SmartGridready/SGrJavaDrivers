package com.smartgridready.driver.j2mod.util;

import java.util.HashMap;
import java.util.Map;

import com.ghgande.j2mod.modbus.net.AbstractSerialConnection;

public class ParityMapper {
	
	private static final Map<com.smartgridready.driver.api.modbus.Parity, Integer> PARITY_MAP = new HashMap<>();
	
	static {
		PARITY_MAP.put(com.smartgridready.driver.api.modbus.Parity.NONE, AbstractSerialConnection.NO_PARITY);
		PARITY_MAP.put(com.smartgridready.driver.api.modbus.Parity.EVEN, AbstractSerialConnection.EVEN_PARITY);
		PARITY_MAP.put(com.smartgridready.driver.api.modbus.Parity.ODD, AbstractSerialConnection.ODD_PARITY);
	}
	
	private ParityMapper() {};
	
	public static Integer map(com.smartgridready.driver.api.modbus.Parity genParity) {
		return PARITY_MAP.getOrDefault(genParity, AbstractSerialConnection.NO_PARITY);		
	}
}
