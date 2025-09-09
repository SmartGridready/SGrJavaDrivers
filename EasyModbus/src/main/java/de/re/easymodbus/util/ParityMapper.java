package de.re.easymodbus.util;

import java.util.HashMap;
import java.util.Map;

import de.re.easymodbus.datatypes.Parity;

/**
 * Maps the {@link com.smartgridready.driver.api.modbus.Parity} enumerations to EasyModbus equivalent.
 */
public class ParityMapper {
    
    private static final Map<com.smartgridready.driver.api.modbus.Parity, Parity> PARITY_MAP = new HashMap<>();
    
    static {
        PARITY_MAP.put(com.smartgridready.driver.api.modbus.Parity.NONE, Parity.None);
        PARITY_MAP.put(com.smartgridready.driver.api.modbus.Parity.EVEN, Parity.Even);
        PARITY_MAP.put(com.smartgridready.driver.api.modbus.Parity.ODD, Parity.Odd);
    }
    
    private ParityMapper() {};
    
    /**
     * Maps parity value.
     * @param genParity the parity value
     * @return an instance of {@link Parity}
     */
    public static Parity map(com.smartgridready.driver.api.modbus.Parity genParity) {
        return PARITY_MAP.getOrDefault(genParity, Parity.None);        
    }

}
