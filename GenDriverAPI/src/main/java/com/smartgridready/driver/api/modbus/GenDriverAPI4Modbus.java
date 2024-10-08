package com.smartgridready.driver.api.modbus;

import com.smartgridready.driver.api.common.GenDriverException;

public interface GenDriverAPI4Modbus {

    boolean connect() throws GenDriverException;

    void disconnect() throws GenDriverException;

    boolean isConnected();
	
	default void setUnitIdentifier( short ident ) {};
	
    int[] ReadInputRegisters(int startingAddress, int quantity) throws GenDriverException, GenDriverSocketException, GenDriverModbusException;
    
    int[] ReadHoldingRegisters(int startingAddress, int quantity) throws GenDriverException, GenDriverSocketException, GenDriverModbusException;
        
    boolean[] ReadDiscreteInputs(int startingAddress, int quantity) throws GenDriverException, GenDriverSocketException, GenDriverModbusException;

    boolean[] ReadCoils(int startingAddress, int quantity) throws GenDriverException, GenDriverSocketException, GenDriverModbusException;
       
    void  WriteMultipleCoils(int startingAdress, boolean[] values) throws GenDriverException, GenDriverSocketException, GenDriverModbusException;
    
    void  WriteSingleCoil(int startingAdress, boolean value) throws GenDriverException, GenDriverSocketException, GenDriverModbusException;
     
    void  WriteMultipleRegisters(int startingAdress, int[] values) throws GenDriverException, GenDriverSocketException, GenDriverModbusException; 
     
    void WriteSingleRegister(int startingAdress, int value) throws GenDriverException, GenDriverSocketException, GenDriverModbusException;
}