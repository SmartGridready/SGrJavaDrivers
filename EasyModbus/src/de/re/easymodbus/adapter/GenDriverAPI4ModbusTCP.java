package de.re.easymodbus.adapter;
/**
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


/**
* <!-- begin-user-doc -->
* <!-- end-user-doc -->
* @generated NOT
**/

import java.io.IOException;

import communicator.common.runtime.GenDriverAPI4Modbus;
import communicator.common.runtime.GenDriverException;
import de.re.easymodbus.exceptions.ModbusException;
import de.re.easymodbus.modbusclient.ModbusClient;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

/*   AUTHOR: IBT / Chris Broennimann for Verein SmartGridready
 * This file hosts the interface from the CommHandler4Modbus implementation into any modbus driver linked to 
 */

public class GenDriverAPI4ModbusTCP implements GenDriverAPI4Modbus {


	ModbusClient mbDevice = new ModbusClient();
	
	public void initDevice(String sIP4Address, int iPort) throws GenDriverException
	{		
		try {
			mbDevice.Connect(sIP4Address,iPort);
		} catch (IOException e) {
			throw new GenDriverException("Init device failed.", e);
		}
	}

    public int[] ReadHoldingRegisters(int startingAddress, int quantity) throws GenDriverException
    {   
    	try {
			return mbDevice.ReadHoldingRegisters(startingAddress,quantity);
		} catch (ModbusException | IOException | SerialPortException | SerialPortTimeoutException e) {
			throw new GenDriverException("Read holding registers failed.", e);
		}
    	
    }
	
    public int[] ReadInputRegisters(int startingAddress, int quantity) throws GenDriverException
    {   
		try {
			return mbDevice.ReadInputRegisters(startingAddress,quantity);
		} catch (ModbusException | IOException | SerialPortException | SerialPortTimeoutException e) {
			throw new GenDriverException("Read input registers failed.", e);
		}
    }
    

    public boolean[] ReadDiscreteInputs(int startingAddress, int quantity) throws GenDriverException
    {   
    	try {
			return mbDevice.ReadDiscreteInputs(startingAddress,quantity);
		} catch (ModbusException | IOException | SerialPortException | SerialPortTimeoutException e) {
			throw new GenDriverException("Read discrete inputs failed.", e);			
		}
    }

    public boolean[] ReadCoils(int startingAddress, int quantity) throws GenDriverException
    {   
		try {
			return mbDevice.ReadCoils(startingAddress,quantity);
		} catch (ModbusException | IOException | SerialPortException | SerialPortTimeoutException e) {
			throw new GenDriverException("Read coils failed.", e);
		}
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

    public void  WriteMultipleCoils(int startingAdress, boolean[] values) throws GenDriverException 
    {
        try {
			mbDevice.WriteMultipleCoils(startingAdress,values);
		} catch (ModbusException | IOException | SerialPortException | SerialPortTimeoutException e) {
			throw new GenDriverException("Write multiple coils failed.", e);
		}  
    }
    
    public void  WriteSingleCoil(int startingAdress, boolean value) throws GenDriverException 
    {
        try {
			mbDevice.WriteSingleCoil(startingAdress, value);
		} catch (ModbusException | IOException | SerialPortException | SerialPortTimeoutException e) {
			throw new GenDriverException("Read coils failed.", e);
		}
    }

     public void  WriteMultipleRegisters(int startingAdress, int[] values) throws GenDriverException 
     {
		 try {
			mbDevice.WriteMultipleRegisters(startingAdress, values);
		} catch (ModbusException | IOException | SerialPortException | SerialPortTimeoutException e) {
			throw new GenDriverException("Write multiple registers failed.", e);
		} 
     }
     
     public void WriteSingleRegister(int startingAdress, int value) throws GenDriverException
     {
		try {
			mbDevice.WriteSingleRegister(startingAdress, value);
		} catch (ModbusException | IOException | SerialPortException | SerialPortTimeoutException e) {
			throw new GenDriverException("Write single register failed.", e);
		}   
     }
     
     /*
     public void 
		mbDevice.setConnectionTimeout(0);   ;
		mbDevice.setLogFileName(null);   ;
		boolean mbDevice.isConnected()   ;
     */
	
}
