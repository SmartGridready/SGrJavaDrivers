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
use their own Modbus RTU drivers
 */


/**
 * <!-- begin-user-doc -->

 * <!-- end-user-doc -->
 * @generated NOT
 * 
 * 
**/

import java.io.IOException;

import communicator.common.runtime.GenDriverAPI4Modbus;
import communicator.common.runtime.GenDriverException;
import de.re.easymodbus.datatypes.Parity;
import de.re.easymodbus.datatypes.StopBits;
import de.re.easymodbus.exceptions.ModbusException;
import de.re.easymodbus.modbusclient.ModbusClient;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

public class GenDriverAPI4ModbusRTU implements GenDriverAPI4Modbus {
	
	ModbusClient mbRTU = new ModbusClient();
	
	public boolean initTrspService(String sCOM)
	{
	    try 
	    {          
	    	mbRTU.setSerialFlag(true);
	    	mbRTU.setSerialPort(sCOM);
	    	mbRTU.setBaudrate(19200);
	    	mbRTU.setParity(Parity.Even);
	    	mbRTU.setStopBits(StopBits.One);
	    	// mbRTU.setLogFileName("easyModebusRTULogger.txt",true);   
	    	// REMARK: to set datalogging active by setting the second parameter to true
	    	mbRTU.Connect(sCOM);
	    	mbRTU.setConnectionTimeout(1500);
	         
	    } 
	   	   
        catch (SerialPortException ex) 
	    {
            System.out.println(ex);
            return false;
        }
        catch (Exception e)
	    {
          e.printStackTrace();
          return false;
         }
	    return true;
    }
	
    
    public void setUnitIdentifier(short unitIdentifier) {
	   mbRTU.setUnitIdentifier(unitIdentifier);
    }
    
    public int[] ReadHoldingRegisters(int startingAddress, int quantity) throws GenDriverException
    {    	
    	try {
			return mbRTU.ReadHoldingRegisters(startingAddress,quantity);
		} catch (ModbusException | IOException | SerialPortException | SerialPortTimeoutException e) {
			throw new GenDriverException("Read holding registers failed.", e);
		} 
    }

    public int[] ReadInputRegisters(int startingAddress, int quantity) throws GenDriverException
    {
    	try {
			return mbRTU.ReadInputRegisters(startingAddress,quantity);
		} catch (ModbusException | IOException | SerialPortException | SerialPortTimeoutException e) {
			throw new GenDriverException("Read input registers failed.", e);			
		}
    }

    public boolean[] ReadDiscreteInputs(int startingAddress, int quantity) throws GenDriverException
    {  
	    try {
			return mbRTU.ReadDiscreteInputs(startingAddress,quantity);
		} catch (ModbusException | IOException | SerialPortException | SerialPortTimeoutException e) {
			throw new GenDriverException("Read discrete inputs failed.", e);
	    }
    }

    public boolean[] ReadCoils(int startingAddress, int quantity) throws GenDriverException
    {   
		try {
			return mbRTU.ReadCoils(startingAddress,quantity);
		} catch (ModbusException | IOException | SerialPortException | SerialPortTimeoutException e) {
			throw new GenDriverException("Read coils failed.", e);
		}
    }    

    public void  WriteMultipleCoils(int startingAdress, boolean[] values) throws GenDriverException 
    {    	
    	try {
			mbRTU.WriteMultipleCoils(startingAdress,values);
		} catch (ModbusException | IOException | SerialPortException | SerialPortTimeoutException e) {
			throw new GenDriverException("Write multiple coils failed.", e);
		}  
    }
    
    public void  WriteSingleCoil(int startingAdress, boolean value) throws GenDriverException 
    {
    	try {
			mbRTU.WriteSingleCoil(startingAdress, value);
		} catch (ModbusException | IOException | SerialPortException | SerialPortTimeoutException e) {
			throw new GenDriverException("Write single coil failed.", e);
		}
    }

     public void  WriteMultipleRegisters(int startingAdress, int[] values) throws GenDriverException 
     {
    	 try {
			mbRTU.WriteMultipleRegisters(startingAdress, values);
		} catch (ModbusException | IOException | SerialPortException | SerialPortTimeoutException e) {
			throw new GenDriverException("Write multiple registers failed.", e);		} 
     }
     
     public void WriteSingleRegister(int startingAdress, int value) throws GenDriverException
     {
    	 try {
			mbRTU.WriteSingleRegister(startingAdress, value);
		} catch (ModbusException | IOException | SerialPortException | SerialPortTimeoutException e) {
			throw new GenDriverException("Write single register failed.", e);
		}       	 
     }
     
	 public void disconnect() throws GenDriverException 
	 {
		try {
			mbRTU.Disconnect();
		}

		catch (Exception e) {
			e.printStackTrace();
			throw new GenDriverException("Disconnect failed.", e);
	 	}
	 }
	 
    public void test()
    {
    	 int[] responseint;
    	 try
    	 {
         mbRTU.setUnitIdentifier((byte)11);
         responseint = mbRTU.ReadHoldingRegisters(23308,16);
         System.out.println("ABBMeter I[L1] = " + (((responseint[0]*65536) +responseint[1])/100.0)+" A"); 
         System.out.println("ABBMeter I[L2] = " + (((responseint[2]*65536) + responseint[3])/100.0)+" A"); 
         System.out.println("ABBMeter I[L3] = " + (((responseint[4]*65536) + responseint[5])/100.0)+" A"); 
         //System.out.println("ABBMeter I[N]  = " + (((responseint[6]*65536) + responseint[7])/100.0)+" A"); 
         //System.out.println("ABBMeter P[tot]= " + (((responseint[8]*65536) +  Math.abs(responseint[9])/100.0))+" W"); 
         System.out.println("ABBMeter P[L1] = " + (((responseint[10]*65536) +  Math.abs(responseint[11]))/100.0)+" W"); 
         System.out.println("ABBMeter P[L2] = " + (((responseint[12]*65536) +  Math.abs(responseint[13]))/100.0)+" W"); 
         System.out.println("ABBMeter P[L3] = " + (((responseint[14]*65536) +  Math.abs(responseint[15]))/100.0)+" W");
    	 }
    	 
         catch (SerialPortException ex) 
 	     {
             System.out.println(ex);
         }
         catch (Exception e)
 	     {
           e.printStackTrace();
         }
    }	 
}
