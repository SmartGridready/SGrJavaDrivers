package de.re.easymodbus.adapter;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import communicator.common.runtime.GenDriverAPI4Modbus;
import communicator.common.runtime.GenDriverException;
import de.re.easymodbus.modbusclient.ModbusClient;
import jssc.SerialPortException;

class GenDriverAPI4ModbusRTUTest {
	private static final Logger LOG = LogManager.getLogger(GenDriverAPI4ModbusRTUTest.class);
	
	private final int[] EXPECTED_RESPONSE = new int[] {0xAA, 2};
	
	@Mock
	ModbusClient modbusClient;
	
    @BeforeEach 
    public void initMocks() {
       MockitoAnnotations.openMocks(this);
    }
	
	@Test
	void readInputRegisters_success() throws Exception {
						
		
		GenDriverAPI4Modbus driver = new GenDriverAPI4ModbusRTU();
		setFieldByReflection(driver, "mbRTU", modbusClient);
		
		when(modbusClient.ReadHoldingRegisters(EXPECTED_RESPONSE[0], EXPECTED_RESPONSE[1])).thenReturn(EXPECTED_RESPONSE);
				
		int[] result = driver.ReadHoldingRegisters(EXPECTED_RESPONSE[0], EXPECTED_RESPONSE[1] );
		reportResult("Successful read register", result);
		assertArrayEquals(EXPECTED_RESPONSE, result);			
	}
	
	
	@Test
	void readInputRegisters_throws_SerialPortException() throws Exception {
						
		
		GenDriverAPI4Modbus driver = new GenDriverAPI4ModbusRTU();
		setFieldByReflection(driver, "mbRTU", modbusClient);
		
		when(modbusClient.ReadHoldingRegisters(EXPECTED_RESPONSE[0], EXPECTED_RESPONSE[1])).thenThrow(new SerialPortException("COM9", "write", SerialPortException.TYPE_PORT_NOT_OPENED));
				
		GenDriverException e = assertThrows(GenDriverException.class, () ->
			driver.ReadHoldingRegisters(EXPECTED_RESPONSE[0], EXPECTED_RESPONSE[1]));
				
		assertEquals("Modbus read error: Port name - COM9; Method name - write; Exception type - Port not opened.", e.getMessage());		
	}
	

	private void reportResult(String testCase, int[] result) {
		StringBuffer sbuf = new StringBuffer();
		Arrays.stream(result).boxed().forEach( b -> sbuf.append(String.format("%x, ", b)));		
		LOG.info(testCase + " - result: {}", sbuf.toString());			
	}
	
	
	private void setFieldByReflection(Object object, String fieldName, Object value) 
			throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		Field f1 = object.getClass().getDeclaredField(fieldName);
		
		f1.setAccessible(true);
		f1.set(object, value);
	}
	
}
