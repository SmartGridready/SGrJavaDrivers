package com.smartgridready.driver.j2mod;

import com.smartgridready.driver.api.modbus.GenDriverAPI4Modbus;
import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.AbstractModbusMaster;
import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.ghgande.j2mod.modbus.procimg.Register;
import com.ghgande.j2mod.modbus.procimg.SimpleRegister;
import com.ghgande.j2mod.modbus.util.BitVector;
import com.smartgridready.driver.api.common.GenDriverException;
import com.smartgridready.driver.api.modbus.GenDriverModbusException;
import com.smartgridready.driver.api.modbus.GenDriverSocketException;

public class J2ModModbusClient<T extends AbstractModbusMaster> implements GenDriverAPI4Modbus {

	private final T mbDevice;
	private short unitId;

	public J2ModModbusClient(T mbDevice) {
		this.mbDevice = mbDevice;
		this.unitId = 0;
	}

	@Override
	public void setUnitIdentifier(short unitId) {
		this.unitId = unitId;
	}

	@Override
	public int[] ReadHoldingRegisters(int startingAddress, int quantity)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		return readHoldingRegisters(unitId, startingAddress, quantity);
	}

	@Override
	public int[] ReadInputRegisters(int startingAddress, int quantity)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		return readInputRegisters(unitId, startingAddress, quantity);
	}

	@Override
	public boolean[] ReadDiscreteInputs(int startingAddress, int quantity)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		return readDiscreteInputs(unitId, startingAddress, quantity);
	}

	@Override
	public boolean[] ReadCoils(int startingAddress, int quantity)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		return readCoils(unitId, startingAddress, quantity);
	}

	@Override
	public void WriteMultipleCoils(int startingAdress, boolean[] values)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		writeMultipleCoils(unitId, startingAdress, values);
	}

	@Override
	public void WriteSingleCoil(int startingAdress, boolean value)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		writeSingleCoil(unitId, startingAdress, value);
	}

	@Override
	public void WriteMultipleRegisters(int startingAdress, int[] values)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		writeMultipleRegisters(unitId, startingAdress, values);
	}

	@Override
	public void WriteSingleRegister(int startingAdress, int value)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		writeSingleRegister(unitId, startingAdress, value);
	}

	@Override
	public int[] readHoldingRegisters(short unitId, int startingAddress, int quantity)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		try {
			Register[] res = mbDevice.readMultipleRegisters(unitId, startingAddress, quantity);
			return convertRegistersToValues(res);
		} catch (ModbusException e) {
			throw new GenDriverModbusException("Error reading holding registers", e);
		}
	}

	@Override
	public int[] readInputRegisters(short unitId, int startingAddress, int quantity)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		try {
			InputRegister[] res = mbDevice.readInputRegisters(unitId, startingAddress, quantity);
			return convertRegistersToValues(res);
		} catch (ModbusException e) {
			throw new GenDriverModbusException("Error reading input registers", e);
		}
	}

	@Override
	public boolean[] readDiscreteInputs(short unitId, int startingAddress, int quantity)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		try {
			BitVector res = mbDevice.readInputDiscretes(unitId, startingAddress, quantity);
			return convertBitVectorToValues(res);
		} catch (ModbusException e) {
			throw new GenDriverModbusException("Error reading discrete inputs", e);
		}
	}

	@Override
	public boolean[] readCoils(short unitId, int startingAddress, int quantity)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		try {
			BitVector res = mbDevice.readCoils(unitId, startingAddress, quantity);
			return convertBitVectorToValues(res);
		} catch (ModbusException e) {
			throw new GenDriverModbusException("Error reading coils", e);
		}
	}

	@Override
	public void writeMultipleCoils(short unitId, int startingAdress, boolean[] values)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		try {
			BitVector bv = convertValuesToBitVector(values);
			mbDevice.writeMultipleCoils(unitId, bv);
		} catch (ModbusException e) {
			throw new GenDriverModbusException("Error writing coils", e);
		}
	}

	@Override
	public void writeSingleCoil(short unitId, int startingAdress, boolean value)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		try {
			mbDevice.writeCoil(unitId, startingAdress, value);
		} catch (ModbusException e) {
			throw new GenDriverModbusException("Error writing coil", e);
		}
	}

	@Override
	public void writeMultipleRegisters(short unitId, int startingAdress, int[] values)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		try {
			Register[] registers = convertValuesToRegisters(values);
			mbDevice.writeMultipleRegisters(unitId, startingAdress, registers);
		} catch (ModbusException e) {
			throw new GenDriverModbusException("Error writing registers", e);
		}
	}

	@Override
	public void writeSingleRegister(short unitId, int startingAdress, int value)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		try {
			mbDevice.writeSingleRegister(unitId, startingAdress, new SimpleRegister(value));
		} catch (ModbusException e) {
			throw new GenDriverModbusException("Error writing register", e);
		}
	}

	@Override
	public boolean connect() throws GenDriverException {
		try {
			mbDevice.connect();
			return mbDevice.isConnected();
		} catch (Exception e) {
			throw new GenDriverException("Connect failed.", e);
		}
	}

	@Override
	public void disconnect() throws GenDriverException {
		try {
			mbDevice.disconnect();
		} catch (Exception e) {
			throw new GenDriverException("Disconnect failed.", e);
		}
	}

	@Override
	public boolean isConnected() {
		return mbDevice.isConnected();
	}

	protected static <T extends InputRegister> int[] convertRegistersToValues(T[] registers) {
		int[] values = new int[registers.length];
		for (int i = 0; i < registers.length; i++) {
			values[i] = registers[i].getValue();
		}
		return values;
	}

	protected static Register[] convertValuesToRegisters(int[] values) {
		Register[] registers = new Register[values.length];
		for (int i = 0; i < values.length; i++) {
			registers[i] = new SimpleRegister(values[i]);
		}
		return registers;
	}

	protected static boolean[] convertBitVectorToValues(BitVector bv) {
		int n = bv.size();
		boolean[] values = new boolean[n];
		for (int i = 0; i < n; i++) {
			values[i] = bv.getBit(i);
		}
		return values;
	}

	protected static BitVector convertValuesToBitVector(boolean[] values) {
		int n = values.length;
		BitVector bv = new BitVector(n);
		for (int i = 0; i < n; i++) {
			bv.setBit(i, values[i]);
		}
		return bv;
	}
}
