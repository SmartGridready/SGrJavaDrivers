package com.smartgridready.driver.j2mod;

import com.smartgridready.driver.api.modbus.GenDriverAPI4Modbus;
import com.smartgridready.driver.api.modbus.GenDriverModbusException;
import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.ModbusSerialMaster;
import com.ghgande.j2mod.modbus.procimg.Register;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class J2ModModbusClientRtuTest {

	private final short UNIT_ID = 1;
	private final int[] EXPECTED_RESPONSE = new int[] {0xAA, 2};

	@Mock
	ModbusSerialMaster modbusClient;

	@BeforeEach
	public void initMocks() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void readInputRegisters_success() throws Exception {
		GenDriverAPI4Modbus driver = new J2ModModbusClient<>(modbusClient);

		final Register[] EXPECTED_RESPONSE_REGS = J2ModModbusClient.convertValuesToRegisters(EXPECTED_RESPONSE);
		when(modbusClient.readMultipleRegisters(UNIT_ID, EXPECTED_RESPONSE[0], EXPECTED_RESPONSE[1])).thenReturn(EXPECTED_RESPONSE_REGS);

		int[] result = driver.readHoldingRegisters(UNIT_ID, EXPECTED_RESPONSE[0], EXPECTED_RESPONSE[1]);
		assertArrayEquals(EXPECTED_RESPONSE, result);
	}

	@Test
	void readInputRegisters_throws_Exception() throws Exception {
		GenDriverAPI4Modbus driver = new J2ModModbusClient<>(modbusClient);

		when(modbusClient.readMultipleRegisters(UNIT_ID, EXPECTED_RESPONSE[0], EXPECTED_RESPONSE[1])).thenThrow(new ModbusException("Serial port not connected"));

		GenDriverModbusException e = assertThrows(GenDriverModbusException.class, () ->
			driver.readHoldingRegisters(UNIT_ID, EXPECTED_RESPONSE[0], EXPECTED_RESPONSE[1]));

		assertEquals("Error reading holding registers", e.getMessage());
	}

	@Test
	void initTrspServiceModbusRTU() throws Exception {
		GenDriverAPI4Modbus driver = new J2ModModbusClient<>(modbusClient);
		driver.connect();
		driver.disconnect();
	}
}
