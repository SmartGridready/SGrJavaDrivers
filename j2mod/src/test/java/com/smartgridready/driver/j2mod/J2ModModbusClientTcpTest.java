package com.smartgridready.driver.j2mod;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.net.Inet4Address;
import java.net.InetAddress;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.ModbusTCPMaster;
import com.ghgande.j2mod.modbus.procimg.SimpleProcessImage;
import com.ghgande.j2mod.modbus.procimg.SimpleRegister;
import com.ghgande.j2mod.modbus.slave.ModbusSlave;
import com.ghgande.j2mod.modbus.slave.ModbusSlaveFactory;
import com.smartgridready.driver.api.modbus.GenDriverAPI4Modbus;

class J2ModModbusClientTcpTest {

	private static final int POOL_SIZE = 3;
	
	private final InetAddress ADDRESS = Inet4Address.getLoopbackAddress();
	private final int PORT = 9991;
	private final short UNIT_ID = 1;
	private final int READ_REG_ADDR = 10000;
	private final int WRITE_REG_ADDR = 10002;
	private final int[] EXPECTED_RESPONSE = new int[] { 0xAA, 2 };
	private final int[] WRITE_REGS = new int[] { 43, 12 };

	private ModbusSlave slave;

	@BeforeEach
	void setup() throws ModbusException {
		this.slave = startSlave();
	}

	@AfterEach
	void tearDown() {
		if (this.slave != null) {
			this.slave.close();
			this.slave = null;
		}
	}
	
	@Test
	void testReadInputRegistersSuccess() throws Exception {

		GenDriverAPI4Modbus driver = new J2ModModbusClient<>(new ModbusTCPMaster(ADDRESS.getHostAddress(), PORT));
		driver.connect();
		assertTrue(driver.isConnected());		
		
		int[] result = driver.readHoldingRegisters(UNIT_ID, READ_REG_ADDR, 2);
		assertArrayEquals(EXPECTED_RESPONSE, result);
		
		driver.disconnect();
		assertFalse(driver.isConnected());
	}

	@Test
	void testWriteHoldingRegistersSuccess() throws Exception {

		GenDriverAPI4Modbus driver = new J2ModModbusClient<>(new ModbusTCPMaster(ADDRESS.getHostAddress(), PORT));
		driver.connect();
		assertTrue(driver.isConnected());		
		
		driver.writeMultipleRegisters(UNIT_ID, WRITE_REG_ADDR, WRITE_REGS);
		assertEquals(WRITE_REGS[0], slave.getProcessImage(UNIT_ID).getRegister(WRITE_REG_ADDR).getValue());
		assertEquals(WRITE_REGS[1], slave.getProcessImage(UNIT_ID).getRegister(WRITE_REG_ADDR+1).getValue());
		
		driver.disconnect();
		assertFalse(driver.isConnected());
	}

	private ModbusSlave startSlave() throws ModbusException {
		ModbusSlave slave = ModbusSlaveFactory.createTCPSlave(Inet4Address.getLoopbackAddress(), PORT, POOL_SIZE, false);

		SimpleProcessImage pi = new SimpleProcessImage();
		pi.addRegister(10000, new SimpleRegister(0xAA));
		pi.addRegister(10001, new SimpleRegister(2));
		pi.addRegister(10002, new SimpleRegister(0));
		pi.addRegister(10003, new SimpleRegister(0));

		slave.addProcessImage(UNIT_ID, pi);
		slave.open();

		return slave;
	}
}
