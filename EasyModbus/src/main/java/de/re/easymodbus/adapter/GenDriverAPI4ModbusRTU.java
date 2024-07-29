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

import com.smartgridready.driver.modbus.api.GenDriverAPI4Modbus;
import com.smartgridready.driver.modbus.api.GenDriverException;
import com.smartgridready.driver.modbus.api.GenDriverModbusException;
import com.smartgridready.driver.modbus.api.GenDriverSocketException;
import com.smartgridready.driver.modbus.api.Parity;
import com.smartgridready.driver.modbus.api.StopBits;
import com.smartgridready.driver.modbus.api.DataBits;
import de.re.easymodbus.modbusclient.ModbusClient;
import de.re.easymodbus.util.DatabitMapper;
import de.re.easymodbus.util.ParityMapper;
import de.re.easymodbus.util.StopbitMapper;

public class GenDriverAPI4ModbusRTU implements GenDriverAPI4Modbus {

	private final ModbusClient mbRTU = new ModbusClient();
	
	@Override
	public boolean initTrspService(String comPort) throws GenDriverException {
		return initTrspService(comPort, 9200 );
	}
	
	@Override
	public boolean initTrspService(String comPort, int baudRate) throws GenDriverException {
		return initTrspService(comPort, baudRate, Parity.EVEN);
	}
	
	@Override
	public boolean initTrspService(String comPort, int baudRate, Parity parity) throws GenDriverException {
		return initTrspService(comPort, baudRate, parity, DataBits.EIGHT);
	}

	@Override
	public boolean initTrspService(String comPort, int baudRate, Parity parity, DataBits dataBits) throws GenDriverException {
		return initTrspService(comPort, baudRate, parity, dataBits, StopBits.ONE);
	}
	
	@Override
	public boolean initTrspService(String sCOM, int baudRate, Parity parity, DataBits dataBits, StopBits stopBits) throws GenDriverException
	{
	    try 
	    {          
	    	mbRTU.setSerialFlag(true);
	    	mbRTU.setSerialPort(sCOM);
	    	mbRTU.setBaudrate(baudRate);
	    	mbRTU.setParity(ParityMapper.map(parity));
			mbRTU.setDataBits(DatabitMapper.map(dataBits));
	    	mbRTU.setStopBits(StopbitMapper.map(stopBits));
	    	mbRTU.Connect(sCOM);
	    	mbRTU.setConnectionTimeout(1500);
	    } 
		catch (Exception e)
	    {
			throw new GenDriverException("Connect to serial port failed.", e);
		}
	    return true;
    }
	
    @Override
    public void setUnitIdentifier(short unitIdentifier) {
	   mbRTU.setUnitIdentifier(unitIdentifier);
    }
    
    public int[] ReadHoldingRegisters(int startingAddress, int quantity) throws GenDriverException, GenDriverSocketException, GenDriverModbusException
    {    	
    	return new ModbusCallHandler<>(
    			mbRTU, 
    			mbRTU::ReadHoldingRegisters).read(startingAddress, quantity);
    }

    public int[] ReadInputRegisters(int startingAddress, int quantity) throws GenDriverException, GenDriverSocketException, GenDriverModbusException
    {
    	return new ModbusCallHandler<>(
    			mbRTU, 
    			mbRTU::ReadInputRegisters).read(startingAddress, quantity); 
    }

    public boolean[] ReadDiscreteInputs(int startingAddress, int quantity) throws GenDriverException, GenDriverSocketException, GenDriverModbusException
    {  
    	return new ModbusCallHandler<>(
    			mbRTU, 
    			mbRTU::ReadDiscreteInputs).read(startingAddress, quantity);     	
    }

    public boolean[] ReadCoils(int startingAddress, int quantity) throws GenDriverException, GenDriverSocketException, GenDriverModbusException
    {   
       	return new ModbusCallHandler<>(
    			mbRTU, 
    			mbRTU::ReadCoils).read(startingAddress, quantity);     
    }

    public void  WriteMultipleCoils(int startingAdress, boolean[] values) throws GenDriverException, GenDriverSocketException, GenDriverModbusException 
    {    	
    	new ModbusCallHandler<>(
    			mbRTU,
    			mbRTU::WriteMultipleCoils).write(startingAdress, values);
    }
    
    public void  WriteSingleCoil(int startingAdress, boolean value) throws GenDriverException, GenDriverSocketException, GenDriverModbusException 
    {
    	new ModbusCallHandler<>(
    			mbRTU,
    			mbRTU::WriteSingleCoil).write(startingAdress, value);
    }

     public void  WriteMultipleRegisters(int startingAdress, int[] values) throws GenDriverException, GenDriverSocketException, GenDriverModbusException 
     {
     	new ModbusCallHandler<>(
    			mbRTU,
    			mbRTU::WriteMultipleRegisters).write(startingAdress, values);
     }
     
     public void WriteSingleRegister(int startingAdress, int value) throws GenDriverException, GenDriverSocketException, GenDriverModbusException
     {
      	new ModbusCallHandler<>(
    			mbRTU,
    			mbRTU::WriteSingleRegister).write(startingAdress, value);
     }
     
	 public void disconnect() throws GenDriverException 
	 {
		try {
			mbRTU.Disconnect();
		}

		catch (Exception e) {
			throw new GenDriverException("Disconnect failed.", e);
	 	}
	 }
}
