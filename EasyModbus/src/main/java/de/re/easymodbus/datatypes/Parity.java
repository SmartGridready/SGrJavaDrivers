package de.re.easymodbus.datatypes;

/**
 * Defines an enumeration of parity options.
 */
public enum Parity 
{
    /** No parity. */
	None (0),
    /** Even parity. */
	Even (2),
    /** Odd parity. */
	Odd(1);
	
	private final int value;
    
    Parity(int value)
    {
        this.value = value;
    }
    
    /**
     * Gets the internal value.
     * @return an integer
     */
    public int getValue() 
    {
        return value;
    }
}
