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

import java.io.IOException;

import communicator.common.runtime.GenDriverAPI4Modbus;
import communicator.common.runtime.GenDriverException;
import communicator.common.runtime.GenDriverModbusException;
import communicator.common.runtime.GenDriverSocketException;
import de.re.easymodbus.modbusclient.ModbusClient;
import jssc.SerialPortException;

/*   AUTHOR: IBT / Chris Broennimann for Verein SmartGridready
 * This file hosts the interface from the CommHandler4Modbus implementation into any modbus driver linked to 
 */

public class GenDriverAPI4ModbusTCP implements GenDriverAPI4Modbus {
	
	private final ModbusClient mbDevice = new ModbusClient();
	
	@Override
	public void initDevice(String sIP4Address, int iPort) throws GenDriverException
	{						
		try {
			mbDevice.Connect(sIP4Address,iPort);
		} catch (IOException e) {
			throw new GenDriverException("Init device failed.", e);
		}
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
	
    public int[] ReadInputRegisters(int startingAddress, int quantity)
    		throws GenDriverException, GenDriverSocketException, GenDriverModbusException
    {   
		return new ModbusCallHandler<>(
			mbDevice,
			mbDevice::ReadInputRegisters,
			mbDevice::Connect).read(startingAddress, quantity);
    }
    

    public boolean[] ReadDiscreteInputs(int startingAddress, int quantity)
    		throws GenDriverException, GenDriverSocketException, GenDriverModbusException
    {   
		return new ModbusCallHandler<>(
			mbDevice,
			mbDevice::ReadDiscreteInputs,
			mbDevice::Connect).read(startingAddress, quantity);
    }

    public boolean[] ReadCoils(int startingAddress, int quantity)
    		throws GenDriverException, GenDriverSocketException, GenDriverModbusException
    {   
		return new ModbusCallHandler<>(
			mbDevice,
			mbDevice::ReadCoils,
			mbDevice::Connect).read(startingAddress, quantity);
    }
	
    /*
		mbDevice.ReadWriteMultipleRegisters(startingAddress,quantity, writingAddress, responseint)
	
	*/
    
    public void disconnect() throws GenDriverException
    {
    	try {
			mbDevice.Disconnect();
		} catch (IOException | SerialPortException e) {
			throw new GenDriverException("Disconnect failed.", e);
		}
    }

    public void  WriteMultipleCoils(int startingAdress, boolean[] values)
    		throws GenDriverException, GenDriverSocketException, GenDriverModbusException
    {
    	new ModbusCallHandler<>(
   			 mbDevice,
   			 mbDevice::WriteMultipleCoils, 
   			 mbDevice::Connect).write(startingAdress, values);
    }
    
    public void  WriteSingleCoil(int startingAdress, boolean value)
    		throws GenDriverException, GenDriverSocketException, GenDriverModbusException
    {
    	new ModbusCallHandler<>(
  			 mbDevice,
  			 mbDevice::WriteSingleCoil, 
  			 mbDevice::Connect).write(startingAdress, value);
    }

     public void  WriteMultipleRegisters(int startingAdress, int[] values) throws GenDriverException, GenDriverSocketException, GenDriverModbusException
     {
    	new ModbusCallHandler<>(
			 mbDevice,
			 mbDevice::WriteMultipleRegisters, 
			 mbDevice::Connect).write(startingAdress, values);
    	
     }
     
     public void WriteSingleRegister(int startingAdress, int value)
    		 throws GenDriverException, GenDriverSocketException, GenDriverModbusException
     {
     	new ModbusCallHandler<>(
   			 mbDevice,
   			 mbDevice::WriteSingleRegister, 
   			 mbDevice::Connect).write(startingAdress, value);
     }
}
