package de.re.easymodbus.adapter;

import com.smartgridready.driver.api.modbus.DataBits;
import com.smartgridready.driver.api.modbus.GenDriverAPI4Modbus;
import com.smartgridready.driver.api.modbus.GenDriverAPI4ModbusFactory;
import com.smartgridready.driver.api.modbus.Parity;
import com.smartgridready.driver.api.modbus.StopBits;

/**
 * Implements a Modbus interface driver factory based on EasyModbus.
 */
public class GenDriverAPI4ModbusFactoryImpl implements GenDriverAPI4ModbusFactory {

    /**
     * Construct.
     */
    public GenDriverAPI4ModbusFactoryImpl() {}

    /**
     * Creates a serial Modbus RTU transport.
     * @param comPort the serial port name
     * @return a new instance of {@link GenDriverAPI4ModbusRTU}
     */
    @Override
    public GenDriverAPI4Modbus createRtuTransport(String comPort) {
        return new GenDriverAPI4ModbusRTU(comPort);
    }

    /**
     * Creates a serial Modbus RTU transport.
     * @param comPort the serial port name
     * @param baudRate the serial port baud rate
     * @return a new instance of {@link GenDriverAPI4ModbusRTU}
     */
    @Override
    public GenDriverAPI4Modbus createRtuTransport(String comPort, int baudRate) {
        return new GenDriverAPI4ModbusRTU(comPort, baudRate);
    }

    /**
     * Creates a serial Modbus RTU transport.
     * @param comPort the serial port name
     * @param baudRate the serial port baud rate
     * @param parity the serial port parity
     * @return a new instance of {@link GenDriverAPI4ModbusRTU}
     */
    @Override
    public GenDriverAPI4Modbus createRtuTransport(String comPort, int baudRate, Parity parity) {
        return new GenDriverAPI4ModbusRTU(comPort, baudRate, parity);
    }

    /**
     * Creates a serial Modbus RTU transport.
     * @param comPort the serial port name
     * @param baudRate the serial port baud rate
     * @param parity the serial port parity
     * @param dataBits the serial port data bits
     * @return a new instance of {@link GenDriverAPI4ModbusRTU}
     */
    @Override
    public GenDriverAPI4Modbus createRtuTransport(String comPort, int baudRate, Parity parity, DataBits dataBits) {
        return new GenDriverAPI4ModbusRTU(comPort, baudRate, parity, dataBits);
    }

    /**
     * Creates a serial Modbus RTU transport.
     * @param comPort the serial port name
     * @param baudRate the serial port baud rate
     * @param parity the serial port parity
     * @param dataBits the serial port data bits
     * @param stopBits the serial port stop bits
     * @return a new instance of {@link GenDriverAPI4ModbusRTU}
     */
    @Override
    public GenDriverAPI4Modbus createRtuTransport(String comPort, int baudRate, Parity parity, DataBits dataBits, StopBits stopBits) {
        return new GenDriverAPI4ModbusRTU(comPort, baudRate, parity, dataBits, stopBits);
    }

    /**
     * Creates a serial Modbus RTU transport.
     * @param comPort the serial port name
     * @param baudRate the serial port baud rate
     * @param parity the serial port parity
     * @param dataBits the serial port data bits
     * @param stopBits the serial port stop bits
     * @param asciiEncoding use ASCII encoding if true, otherwise RTU encoding
     * @return a new instance of {@link GenDriverAPI4ModbusRTU}
     */
    @Override
    public GenDriverAPI4Modbus createRtuTransport(String comPort, int baudRate, Parity parity, DataBits dataBits, StopBits stopBits, boolean asciiEncoding) {
        if (!asciiEncoding) {
            return new GenDriverAPI4ModbusRTU(comPort, baudRate, parity, dataBits, stopBits);
        }
        throw new UnsupportedOperationException("ASCII encoding not supported");
    }

    @Override
    public GenDriverAPI4Modbus createTcpTransport(String ipAddress) {
        return new GenDriverAPI4ModbusTCP(ipAddress);
    }

    @Override
    public GenDriverAPI4Modbus createTcpTransport(String ipAddress, int port) {
        return new GenDriverAPI4ModbusTCP(ipAddress, port);
    }

    @Override
    public GenDriverAPI4Modbus createUdpTransport(String ipAddress) {
        return new GenDriverAPI4ModbusUDP(ipAddress);
    }

    @Override
    public GenDriverAPI4Modbus createUdpTransport(String ipAddress, int port) {
        return new GenDriverAPI4ModbusUDP(ipAddress, port);
    }
}
