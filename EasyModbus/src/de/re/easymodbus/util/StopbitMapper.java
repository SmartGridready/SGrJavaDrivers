package de.re.easymodbus.util;

import java.util.HashMap;
import java.util.Map;

import de.re.easymodbus.datatypes.StopBits;


public class StopbitMapper {
	private static final Map<communicator.common.runtime.StopBits, StopBits> STOPBIT_MAP = new HashMap<>();
	
	static {
		STOPBIT_MAP.put(communicator.common.runtime.StopBits.ONE, StopBits.One);
		STOPBIT_MAP.put(communicator.common.runtime.StopBits.ONE_AND_HALF, StopBits.OnePointFive);
		STOPBIT_MAP.put(communicator.common.runtime.StopBits.TWO, StopBits.Two);
	}
	
	private StopbitMapper() {};
	
	public static StopBits map(communicator.common.runtime.StopBits genStopBit) {
		return STOPBIT_MAP.getOrDefault(genStopBit, StopBits.One);
	}
}
