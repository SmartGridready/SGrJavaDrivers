package de.re.easymodbus.datatypes;

public enum Parity 
{
	None (0),
	Even (2),
	Odd(1);
	
	private final int value;
    
    Parity(int value)
    {
        this.value = value;
    }
    
    public int getValue() 
    {
        return value;
    }
}
