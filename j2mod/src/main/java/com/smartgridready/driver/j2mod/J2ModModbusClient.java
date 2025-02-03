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
	private int unitIdentifier;

	public J2ModModbusClient(T mbDevice) {
		this.mbDevice = mbDevice;
		this.unitIdentifier = 0;
	}

	@Override
	public void setUnitIdentifier(short unitIdentifier) {
		this.unitIdentifier = unitIdentifier;
	}

	@Override
	public int[] ReadHoldingRegisters(int startingAddress, int quantity)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		try {
			Register[] res = mbDevice.readMultipleRegisters(unitIdentifier, startingAddress, quantity);
			return convertRegistersToValues(res);
		} catch (ModbusException e) {
			throw new GenDriverModbusException("Error reading holding registers", e);
		}
	}

	@Override
	public int[] ReadInputRegisters(int startingAddress, int quantity)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		try {
			InputRegister[] res = mbDevice.readInputRegisters(unitIdentifier, startingAddress, quantity);
			return convertRegistersToValues(res);
		} catch (ModbusException e) {
			throw new GenDriverModbusException("Error reading input registers", e);
		}
	}

	@Override
	public boolean[] ReadDiscreteInputs(int startingAddress, int quantity)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		try {
			BitVector res = mbDevice.readInputDiscretes(unitIdentifier, startingAddress, quantity);
			return convertBitVectorToValues(res);
		} catch (ModbusException e) {
			throw new GenDriverModbusException("Error reading discrete inputs", e);
		}
	}

	@Override
	public boolean[] ReadCoils(int startingAddress, int quantity)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		try {
			BitVector res = mbDevice.readCoils(unitIdentifier, startingAddress, quantity);
			return convertBitVectorToValues(res);
		} catch (ModbusException e) {
			throw new GenDriverModbusException("Error reading coils", e);
		}
	}

	@Override
	public void WriteMultipleCoils(int startingAdress, boolean[] values)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		try {
			BitVector bv = convertValuesToBitVector(values);
			mbDevice.writeMultipleCoils(unitIdentifier, bv);
		} catch (ModbusException e) {
			throw new GenDriverModbusException("Error writing coils", e);
		}
	}

	@Override
	public void WriteSingleCoil(int startingAdress, boolean value)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		try {
			mbDevice.writeCoil(unitIdentifier, startingAdress, value);
		} catch (ModbusException e) {
			throw new GenDriverModbusException("Error writing coil", e);
		}
	}

	@Override
	public void WriteMultipleRegisters(int startingAdress, int[] values)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		try {
			Register[] registers = convertValuesToRegisters(values);
			mbDevice.writeMultipleRegisters(unitIdentifier, startingAdress, registers);
		} catch (ModbusException e) {
			throw new GenDriverModbusException("Error writing registers", e);
		}
	}

	@Override
	public void WriteSingleRegister(int startingAdress, int value)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		try {
			mbDevice.writeSingleRegister(unitIdentifier, startingAdress, new SimpleRegister(value));
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
