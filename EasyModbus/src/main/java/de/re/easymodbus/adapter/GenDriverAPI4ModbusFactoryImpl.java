package de.re.easymodbus.adapter;

import com.smartgridready.driver.api.modbus.DataBits;
import com.smartgridready.driver.api.modbus.GenDriverAPI4Modbus;
import com.smartgridready.driver.api.modbus.GenDriverAPI4ModbusFactory;
import com.smartgridready.driver.api.modbus.Parity;
import com.smartgridready.driver.api.modbus.StopBits;

public class GenDriverAPI4ModbusFactoryImpl implements GenDriverAPI4ModbusFactory {
    
    @Override
    public GenDriverAPI4Modbus createRtuTransport(String comPort) {
        return new GenDriverAPI4ModbusRTU(comPort);
    }

    @Override
    public GenDriverAPI4Modbus createRtuTransport(String comPort, int baudRate) {
        return new GenDriverAPI4ModbusRTU(comPort, baudRate);
    }

    @Override
    public GenDriverAPI4Modbus createRtuTransport(String comPort, int baudRate, Parity parity) {
        return new GenDriverAPI4ModbusRTU(comPort, baudRate, parity);
    }

    @Override
    public GenDriverAPI4Modbus createRtuTransport(String comPort, int baudRate, Parity parity, DataBits dataBits) {
        return new GenDriverAPI4ModbusRTU(comPort, baudRate, parity, dataBits);
    }

    @Override
    public GenDriverAPI4Modbus createRtuTransport(String comPort, int baudRate, Parity parity, DataBits dataBits, StopBits stopBits) {
        return new GenDriverAPI4ModbusRTU(comPort, baudRate, parity, dataBits, stopBits);
    }

    @Override
    public GenDriverAPI4Modbus createTcpTransport(String ipAddress) {
        return new GenDriverAPI4ModbusTCP(ipAddress);
    }

    @Override
    public GenDriverAPI4Modbus createTcpTransport(String ipAddress, int port) {
        return new GenDriverAPI4ModbusTCP(ipAddress, port);
    }
}
