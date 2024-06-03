package de.re.easymodbus.datatypes;

public enum DataBits 
{
	Seven (7),
	Eight (8);
	
	private int value;
    
    private DataBits(int value) 
    {
        this.value = value;
    }
    
    public int getValue() 
    {
        return value;
    }
}
