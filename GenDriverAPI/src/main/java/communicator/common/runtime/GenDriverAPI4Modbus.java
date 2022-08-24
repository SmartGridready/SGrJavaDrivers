package communicator.common.runtime;

public interface GenDriverAPI4Modbus {
	
	public default void setUnitIdentifier( short ident ) {};
	
    public int[] ReadInputRegisters(int startingAddress, int quantity) throws GenDriverException, GenDriverSocketException, GenDriverModbusException;        
    
    public int[] ReadHoldingRegisters(int startingAddress, int quantity) throws GenDriverException, GenDriverSocketException, GenDriverModbusException;
        
    public boolean[] ReadDiscreteInputs(int startingAddress, int quantity) throws GenDriverException, GenDriverSocketException, GenDriverModbusException;
 

    public boolean[] ReadCoils(int startingAddress, int quantity) throws GenDriverException, GenDriverSocketException, GenDriverModbusException;
       
    public void  WriteMultipleCoils(int startingAdress, boolean[] values) throws GenDriverException, GenDriverSocketException, GenDriverModbusException;
    
    public void  WriteSingleCoil(int startingAdress, boolean value) throws GenDriverException, GenDriverSocketException, GenDriverModbusException;
     
    public void  WriteMultipleRegisters(int startingAdress, int[] values) throws GenDriverException, GenDriverSocketException, GenDriverModbusException; 
     
    public void WriteSingleRegister(int startingAdress, int value) throws GenDriverException, GenDriverSocketException, GenDriverModbusException;

    public void disconnect() throws GenDriverException;

	default public boolean initTrspService(String comPort) throws GenDriverException { return true; };

	default public void initDevice(String ipAddress, int port) throws GenDriverException {};
}