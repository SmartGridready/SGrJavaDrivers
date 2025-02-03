package com.smartgridready.driver.j2mod;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.smartgridready.driver.api.modbus.GenDriverAPI4Modbus;

class J2ModModbusClientUdpTest {
	
	private static final Logger LOG = LogManager.getLogger(J2ModModbusClientUdpTest.class);
	
	private final int[] EXPECTED_RESPONSE = new int[] {0xAA, 2};
	
	@Test
	void testReadInputRegistersSuccess() throws Exception {
		
		TestUdpSocketServer server = new TestUdpSocketServer();
		server.start();
		
		GenDriverAPI4Modbus driver = new J2ModModbusClientUdp("127.0.0.1", 9099);
		driver.connect();
		
		int[] result = driver.ReadHoldingRegisters(EXPECTED_RESPONSE[0], EXPECTED_RESPONSE[1] );
		reportResult(result);
		assertArrayEquals(EXPECTED_RESPONSE, result);
		
		server.disconnect();
	}

	private void reportResult(int[] result) {
		StringBuffer sbuf = new StringBuffer();
		Arrays.stream(result).boxed().forEach( b -> sbuf.append(String.format("%x, ", b)));
		LOG.info("Successful read register - result: {}", sbuf.toString());
	}
}
