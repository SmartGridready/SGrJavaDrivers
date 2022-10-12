package communicator.common.runtime;

public interface GenDriverAPI4Modbus {
	
	default void setUnitIdentifier( short ident ) {};
	
    int[] ReadInputRegisters(int startingAddress, int quantity) throws GenDriverException, GenDriverSocketException, GenDriverModbusException;        
    
    int[] ReadHoldingRegisters(int startingAddress, int quantity) throws GenDriverException, GenDriverSocketException, GenDriverModbusException;
        
    boolean[] ReadDiscreteInputs(int startingAddress, int quantity) throws GenDriverException, GenDriverSocketException, GenDriverModbusException;
 

    boolean[] ReadCoils(int startingAddress, int quantity) throws GenDriverException, GenDriverSocketException, GenDriverModbusException;
       
    void  WriteMultipleCoils(int startingAdress, boolean[] values) throws GenDriverException, GenDriverSocketException, GenDriverModbusException;
    
    void  WriteSingleCoil(int startingAdress, boolean value) throws GenDriverException, GenDriverSocketException, GenDriverModbusException;
     
    void  WriteMultipleRegisters(int startingAdress, int[] values) throws GenDriverException, GenDriverSocketException, GenDriverModbusException; 
     
    void WriteSingleRegister(int startingAdress, int value) throws GenDriverException, GenDriverSocketException, GenDriverModbusException;

    void disconnect() throws GenDriverException;

	default boolean initTrspService(String comPort) throws GenDriverException { return true; }
	
	default boolean initTrspService(String comPort, int baudRate) throws GenDriverException {return true; }
	
	default boolean initTrspService(String comPort, int baudRate, Parity parity) throws GenDriverException { return true; }
	
	default boolean initTrspService(String comPort, int baudRate, Parity parity, StopBits stopBits) throws GenDriverException { return true; }

	default void initDevice(String ipAddress, int port) throws GenDriverException {}
}