package de.re.easymodbus.adapter;
/*
*Copyright(c) 2021 Verein SmartGridready Switzerland
* @generated NOT
*
This Open Source Software is BSD 3 clause licensed:
Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in
   the documentation and/or other materials provided with the distribution.
3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from
   this software without specific prior written permission.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
OF THE POSSIBILITY OF SUCH DAMAGE.

It includes completely manually generated code. It is the Interface of the Modbus RTU inside the CommHandler and its purpose is to enable 3rd party programmers to
use their own Modbus TCP drivers
 */

import com.smartgridready.driver.api.modbus.GenDriverAPI4Modbus;
import com.smartgridready.driver.api.common.GenDriverException;
import com.smartgridready.driver.api.modbus.GenDriverModbusException;
import com.smartgridready.driver.api.modbus.GenDriverSocketException;

import de.re.easymodbus.exceptions.ModbusException;
import de.re.easymodbus.modbusclient.ModbusClient;

/*   AUTHOR: IBT / Chris Broennimann for Verein SmartGridready
 * This file hosts the interface from the CommHandler4Modbus implementation into any modbus driver linked to
 */

abstract class GenDriverAPI4ModbusBase implements GenDriverAPI4Modbus {

	protected final ModbusClient mbDevice;

	protected GenDriverAPI4ModbusBase() {
		mbDevice = new ModbusClient();
	}

	@Override
    public void setUnitIdentifier(short unitIdentifier) {
		mbDevice.setUnitIdentifier(unitIdentifier);
    }

	@Override
	public int[] ReadHoldingRegisters(int startingAddress, int quantity)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException
	{
		return new ModbusCallHandler<>(
			mbDevice,
			mbDevice::ReadHoldingRegisters,
			mbDevice::Connect).read(startingAddress, quantity);
	}

	@Override
	public int[] ReadInputRegisters(int startingAddress, int quantity)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException
	{
		return new ModbusCallHandler<>(
			mbDevice,
			mbDevice::ReadInputRegisters,
			mbDevice::Connect).read(startingAddress, quantity);
	}

	@Override
	public boolean[] ReadDiscreteInputs(int startingAddress, int quantity)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException
	{
		return new ModbusCallHandler<>(
			mbDevice,
			mbDevice::ReadDiscreteInputs,
			mbDevice::Connect).read(startingAddress, quantity);
	}

	@Override
	public boolean[] ReadCoils(int startingAddress, int quantity)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException
	{
		return new ModbusCallHandler<>(
			mbDevice,
			mbDevice::ReadCoils,
			mbDevice::Connect).read(startingAddress, quantity);
	}

	@Override
	public void WriteMultipleCoils(int startingAdress, boolean[] values)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException
	{
		new ModbusCallHandler<>(
			 mbDevice,
			 mbDevice::WriteMultipleCoils,
			 mbDevice::Connect).write(startingAdress, values);
	}

	@Override
	public void WriteSingleCoil(int startingAdress, boolean value)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException
	{
		new ModbusCallHandler<>(
			 mbDevice,
			 mbDevice::WriteSingleCoil,
			 mbDevice::Connect).write(startingAdress, value);
	}

	@Override
	public void WriteMultipleRegisters(int startingAdress, int[] values) throws GenDriverException, GenDriverSocketException, GenDriverModbusException
	{
		new ModbusCallHandler<>(
			mbDevice,
			mbDevice::WriteMultipleRegisters,
			mbDevice::Connect).write(startingAdress, values);
	}

	@Override
	public void WriteSingleRegister(int startingAdress, int value)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException
	{
		new ModbusCallHandler<>(
			mbDevice,
			mbDevice::WriteSingleRegister,
			mbDevice::Connect).write(startingAdress, value);
	}

	@Override
	public int[] readHoldingRegisters(short unitId, int startingAddress, int quantity)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		setUnitIdentifier(unitId);
		return new ModbusCallHandler<>(
			mbDevice,
			mbDevice::ReadHoldingRegisters,
			mbDevice::Connect).read(startingAddress, quantity);
	}

	@Override
	public int[] readInputRegisters(short unitId, int startingAddress, int quantity)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		setUnitIdentifier(unitId);
		return new ModbusCallHandler<>(
			mbDevice,
			mbDevice::ReadInputRegisters,
			mbDevice::Connect).read(startingAddress, quantity);
	}

	@Override
	public boolean[] readDiscreteInputs(short unitId, int startingAddress, int quantity)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		setUnitIdentifier(unitId);
		return new ModbusCallHandler<>(
			mbDevice,
			mbDevice::ReadDiscreteInputs,
			mbDevice::Connect).read(startingAddress, quantity);
	}

	@Override
	public boolean[] readCoils(short unitId, int startingAddress, int quantity)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		setUnitIdentifier(unitId);
		return new ModbusCallHandler<>(
			mbDevice,
			mbDevice::ReadCoils,
			mbDevice::Connect).read(startingAddress, quantity);
	}

	@Override
	public void writeMultipleCoils(short unitId, int startingAdress, boolean[] values)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		setUnitIdentifier(unitId);
		new ModbusCallHandler<>(
			 mbDevice,
			 mbDevice::WriteMultipleCoils,
			 mbDevice::Connect).write(startingAdress, values);
	}

	@Override
	public void writeSingleCoil(short unitId, int startingAdress, boolean value)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		setUnitIdentifier(unitId);
		new ModbusCallHandler<>(
			 mbDevice,
			 mbDevice::WriteSingleCoil,
			 mbDevice::Connect).write(startingAdress, value);
	}

	@Override
	public void writeMultipleRegisters(short unitId, int startingAdress, int[] values)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		setUnitIdentifier(unitId);
		new ModbusCallHandler<>(
			mbDevice,
			mbDevice::WriteMultipleRegisters,
			mbDevice::Connect).write(startingAdress, values);
	}

	@Override
	public void writeSingleRegister(short unitId, int startingAdress, int value)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		setUnitIdentifier(unitId);
		new ModbusCallHandler<>(
			mbDevice,
			mbDevice::WriteSingleRegister,
			mbDevice::Connect).write(startingAdress, value);
	}

	@Override
	public boolean connect() throws GenDriverException
	{
		try {
			mbDevice.Connect();
			return mbDevice.isConnected();
		} catch (Exception e) {
			throw new GenDriverException("Connect failed.", e);
	 	}
	}

	@Override
	public void disconnect() throws GenDriverException
	{
		try {
			mbDevice.Disconnect();
		} catch (Exception e) {
			throw new GenDriverException("Disconnect failed.", e);
		}
	}

	@Override
	public boolean isConnected()
	{
		return mbDevice.isConnected();
	}
}
