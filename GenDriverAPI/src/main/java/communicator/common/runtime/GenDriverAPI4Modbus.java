package communicator.common.runtime;

public interface GenDriverAPI4Modbus {
	
	public default void setUnitIdentifier( short ident ) {};
	
    public int[] ReadInputRegisters(int startingAddress, int quantity) throws GenDriverException;        
    
    public int[] ReadHoldingRegisters(int startingAddress, int quantity) throws GenDriverException;
        
    public boolean[] ReadDiscreteInputs(int startingAddress, int quantity) throws GenDriverException;
 

    public boolean[] ReadCoils(int startingAddress, int quantity) throws GenDriverException;
       
    public void  WriteMultipleCoils(int startingAdress, boolean[] values) throws GenDriverException;
    
    public void  WriteSingleCoil(int startingAdress, boolean value) throws GenDriverException;
     
    public void  WriteMultipleRegisters(int startingAdress, int[] values) throws GenDriverException; 
     
    public void WriteSingleRegister(int startingAdress, int value) throws GenDriverException;

    public void disconnect() throws GenDriverException;

	default public boolean initTrspService(String comPort) throws GenDriverException { return true; };

	default public void initDevice(String ipAddress, int port) throws GenDriverException {};
}