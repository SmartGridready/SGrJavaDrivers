package com.smartgridready.driver.api.modbus;

import com.smartgridready.driver.api.common.GenDriverException;

/**
 * Adds generic connect functionality to Modbus driver.
 */
public interface GenDriverAPI4ModbusConnectable extends GenDriverAPI4Modbus {
    
    /**
     * Connects to Modbus transport.
     * @return true if connected, false otherwise
     * @throws GenDriverException
     */
    public boolean connect() throws GenDriverException;

    /**
     * Tells if the transport is connected.
     * @return true if connected, false otherwise
     */
    public boolean isConnected();
}
