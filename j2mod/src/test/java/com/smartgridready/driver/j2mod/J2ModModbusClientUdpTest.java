package com.smartgridready.driver.j2mod;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.Inet4Address;
import java.net.InetAddress;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.ModbusUDPMaster;
import com.ghgande.j2mod.modbus.procimg.SimpleProcessImage;
import com.ghgande.j2mod.modbus.procimg.SimpleRegister;
import com.ghgande.j2mod.modbus.slave.ModbusSlave;
import com.ghgande.j2mod.modbus.slave.ModbusSlaveFactory;
import com.smartgridready.driver.api.modbus.GenDriverAPI4Modbus;

class J2ModModbusClientUdpTest {
	
	private final InetAddress ADDRESS = Inet4Address.getLoopbackAddress();
	private final int PORT = 9992;
	private final short UNIT_ID = 1;
	private final int REG_ADDR = 10000;
	private final int[] EXPECTED_RESPONSE = new int[] { 0xAA, 2 };

	private ModbusSlave slave;
	
	@BeforeEach
	void setup() throws ModbusException {
		this.slave = startSlave();
	}

	@AfterEach
	void tearDown() {
		this.slave = null;
	}

	@Test
	void testReadInputRegistersSuccess() throws Exception {

		GenDriverAPI4Modbus driver = new J2ModModbusClient<>(new ModbusUDPMaster(Inet4Address.getLoopbackAddress().getHostAddress(), PORT));
		driver.setUnitIdentifier(UNIT_ID);
		driver.connect();
		assertTrue(driver.isConnected());		
		
		int[] result = driver.ReadHoldingRegisters(REG_ADDR, 2);
		assertArrayEquals(EXPECTED_RESPONSE, result);
		
		driver.disconnect();
		assertFalse(driver.isConnected());
	}

	private ModbusSlave startSlave() throws ModbusException {
		ModbusSlave slave = ModbusSlaveFactory.createUDPSlave(ADDRESS, PORT);

		SimpleProcessImage pi = new SimpleProcessImage();
		pi.addRegister(10000, new SimpleRegister(0xAA));
		pi.addRegister(10001, new SimpleRegister(2));

		slave.addProcessImage(UNIT_ID, pi);
		slave.open();
		
		return slave;
	}
}
