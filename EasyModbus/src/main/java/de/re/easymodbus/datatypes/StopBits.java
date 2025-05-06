package de.re.easymodbus.datatypes;

/**
 * Defines an enumeration of stop bits options.
 */
public enum StopBits 
{
    /** 1 stop bit. */
	One (1),
    /** 1.5 stop bits. */
	OnePointFive (3),
    /** 2 stop bits. */
	Two (2);
	
	private int value;
    
    private StopBits(int value) 
    {
        this.value = value;
    }
    
    /**
     * Gets the interval value.
     * @return an integer
     */
    public int getValue() 
    {
        return value;
    }
}
