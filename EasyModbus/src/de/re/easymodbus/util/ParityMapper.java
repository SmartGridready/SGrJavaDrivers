package de.re.easymodbus.util;

import java.util.HashMap;
import java.util.Map;

import de.re.easymodbus.datatypes.Parity;

public class ParityMapper {
	
	private static final Map<communicator.common.runtime.Parity, Parity> PARITY_MAP = new HashMap<>();
	
	static {
		PARITY_MAP.put(communicator.common.runtime.Parity.NONE, Parity.None);
		PARITY_MAP.put(communicator.common.runtime.Parity.EVEN, Parity.Even);
		PARITY_MAP.put(communicator.common.runtime.Parity.ODD, Parity.Odd);
	}
	
	private ParityMapper() {};
	
	public static Parity map(communicator.common.runtime.Parity genParity) {		
		return PARITY_MAP.getOrDefault(genParity, Parity.None);		
	}

}
