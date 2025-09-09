package de.re.easymodbus.datatypes;

/**
 * Defines an enumeration of data bits options.
 */
public enum DataBits 
{
    /** 7 bits. */
    Seven (7),
    /** 8 bits. */
    Eight (8);
    
    private int value;
    
    private DataBits(int value) 
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
