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
use their own Modbus RTU drivers
 */

import com.smartgridready.driver.api.modbus.GenDriverAPI4Modbus;
import com.smartgridready.driver.api.common.GenDriverException;
import com.smartgridready.driver.api.modbus.GenDriverModbusException;
import com.smartgridready.driver.api.modbus.GenDriverSocketException;
import com.smartgridready.driver.api.modbus.Parity;
import com.smartgridready.driver.api.modbus.StopBits;
import com.smartgridready.driver.api.modbus.DataBits;
import de.re.easymodbus.modbusclient.ModbusClient;
import de.re.easymodbus.util.DatabitMapper;
import de.re.easymodbus.util.ParityMapper;
import de.re.easymodbus.util.StopbitMapper;

public class GenDriverAPI4ModbusRTU implements GenDriverAPI4Modbus {

	private final ModbusClient mbRTU;

	public GenDriverAPI4ModbusRTU(String comPort, int baudRate, Parity parity, DataBits dataBits, StopBits stopBits) {
		mbRTU = new ModbusClient();
		mbRTU.setSerialFlag(true);
		mbRTU.setSerialPort(comPort);
		mbRTU.setBaudrate(baudRate);
		mbRTU.setParity(ParityMapper.map(parity));
		mbRTU.setDataBits(DatabitMapper.map(dataBits));
		mbRTU.setStopBits(StopbitMapper.map(stopBits));
		mbRTU.setConnectionTimeout(1500);
	}

	public GenDriverAPI4ModbusRTU(String comPort, int baudRate, Parity parity, DataBits dataBits) {
		this(comPort, baudRate, parity, dataBits, StopBits.ONE);
	}

	public GenDriverAPI4ModbusRTU(String comPort, int baudRate, Parity parity) {
		this(comPort, baudRate, parity, DataBits.EIGHT);
	}

	public GenDriverAPI4ModbusRTU(String comPort, int baudRate) {
		this(comPort, baudRate, Parity.EVEN);
	}

	public GenDriverAPI4ModbusRTU(String comPort) {
		this(comPort, 9600);
	}
	
    @Override
    public void setUnitIdentifier(short unitIdentifier) {
	   mbRTU.setUnitIdentifier(unitIdentifier);
    }
    
	@Override
    public int[] ReadHoldingRegisters(int startingAddress, int quantity) throws GenDriverException, GenDriverSocketException, GenDriverModbusException
    {    	
    	return new ModbusCallHandler<>(
    			mbRTU, 
    			mbRTU::ReadHoldingRegisters).read(startingAddress, quantity);
    }

	@Override
    public int[] ReadInputRegisters(int startingAddress, int quantity) throws GenDriverException, GenDriverSocketException, GenDriverModbusException
    {
    	return new ModbusCallHandler<>(
    			mbRTU, 
    			mbRTU::ReadInputRegisters).read(startingAddress, quantity); 
    }

	@Override
    public boolean[] ReadDiscreteInputs(int startingAddress, int quantity) throws GenDriverException, GenDriverSocketException, GenDriverModbusException
    {  
    	return new ModbusCallHandler<>(
    			mbRTU, 
    			mbRTU::ReadDiscreteInputs).read(startingAddress, quantity);     	
    }

	@Override
    public boolean[] ReadCoils(int startingAddress, int quantity) throws GenDriverException, GenDriverSocketException, GenDriverModbusException
    {   
       	return new ModbusCallHandler<>(
    			mbRTU, 
    			mbRTU::ReadCoils).read(startingAddress, quantity);     
    }

	@Override
    public void  WriteMultipleCoils(int startingAdress, boolean[] values) throws GenDriverException, GenDriverSocketException, GenDriverModbusException 
    {    	
    	new ModbusCallHandler<>(
    			mbRTU,
    			mbRTU::WriteMultipleCoils).write(startingAdress, values);
    }
    
	@Override
    public void  WriteSingleCoil(int startingAdress, boolean value) throws GenDriverException, GenDriverSocketException, GenDriverModbusException 
    {
    	new ModbusCallHandler<>(
    			mbRTU,
    			mbRTU::WriteSingleCoil).write(startingAdress, value);
    }

	@Override
    public void  WriteMultipleRegisters(int startingAdress, int[] values) throws GenDriverException, GenDriverSocketException, GenDriverModbusException 
    {
        new ModbusCallHandler<>(
    			mbRTU,
    			mbRTU::WriteMultipleRegisters).write(startingAdress, values);
    }

	@Override
    public void WriteSingleRegister(int startingAdress, int value) throws GenDriverException, GenDriverSocketException, GenDriverModbusException
    {
      	new ModbusCallHandler<>(
    			mbRTU,
    			mbRTU::WriteSingleRegister).write(startingAdress, value);
    }

	@Override
	public boolean connect() throws GenDriverException
	{
		try {
			mbRTU.Connect();
			return mbRTU.isConnected();
		} catch (Exception e) {
			throw new GenDriverException("Connect failed.", e);
	 	}
	}

	@Override
	public void disconnect() throws GenDriverException 
	{
		try {
			mbRTU.Disconnect();
		} catch (Exception e) {
			throw new GenDriverException("Disconnect failed.", e);
	 	}
	}

	@Override
	public boolean isConnected()
	{
		return mbRTU.isConnected();
	}
}
