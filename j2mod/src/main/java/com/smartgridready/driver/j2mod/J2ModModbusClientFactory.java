package com.smartgridready.driver.j2mod;

import com.ghgande.j2mod.modbus.facade.ModbusSerialMaster;
import com.ghgande.j2mod.modbus.facade.ModbusTCPMaster;
import com.ghgande.j2mod.modbus.facade.ModbusUDPMaster;
import com.ghgande.j2mod.modbus.net.AbstractSerialConnection;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import com.smartgridready.driver.api.modbus.DataBits;
import com.smartgridready.driver.api.modbus.GenDriverAPI4Modbus;
import com.smartgridready.driver.api.modbus.GenDriverAPI4ModbusFactory;
import com.smartgridready.driver.api.modbus.Parity;
import com.smartgridready.driver.api.modbus.StopBits;
import com.smartgridready.driver.j2mod.util.DatabitMapper;
import com.smartgridready.driver.j2mod.util.ParityMapper;
import com.smartgridready.driver.j2mod.util.StopbitMapper;

public class J2ModModbusClientFactory implements GenDriverAPI4ModbusFactory {

    private static final int DEFAULT_TIMEOUT = 5000;
	private static final int DEFAULT_PORT = 502;
    private static final boolean DEFAULT_RECONNECT = true;
    private static final boolean DEFAULT_RTU_OVER_TCP = false;
    private static final boolean DEFAULT_ECHO = false;
    private static final int DEFAULT_BAUD = 9600;

    
    @Override
    public GenDriverAPI4Modbus createRtuTransport(String comPort) {
        return new J2ModModbusClient<>(
            new ModbusSerialMaster(
                getSerialParameters(comPort),
                DEFAULT_TIMEOUT
            )
        );
    }

    @Override
    public GenDriverAPI4Modbus createRtuTransport(String comPort, int baudRate) {
        return new J2ModModbusClient<>(
            new ModbusSerialMaster(
                getSerialParameters(comPort, baudRate),
                DEFAULT_TIMEOUT
            )
        );
    }

    @Override
    public GenDriverAPI4Modbus createRtuTransport(String comPort, int baudRate, Parity parity) {
        return new J2ModModbusClient<>(
            new ModbusSerialMaster(
                getSerialParameters(comPort, baudRate, parity),
                DEFAULT_TIMEOUT
            )
        );
    }

    @Override
    public GenDriverAPI4Modbus createRtuTransport(String comPort, int baudRate, Parity parity, DataBits dataBits) {
        return new J2ModModbusClient<>(
            new ModbusSerialMaster(
                getSerialParameters(comPort, baudRate, parity, dataBits),
                DEFAULT_TIMEOUT
            )
        );
    }

    @Override
    public GenDriverAPI4Modbus createRtuTransport(String comPort, int baudRate, Parity parity, DataBits dataBits, StopBits stopBits) {
        return new J2ModModbusClient<>(
            new ModbusSerialMaster(
                getSerialParameters(comPort, baudRate, parity, dataBits, stopBits),
                DEFAULT_TIMEOUT
            )
        );
    }

    @Override
    public GenDriverAPI4Modbus createTcpTransport(String ipAddress) {
        return new J2ModModbusClient<>(
            new ModbusTCPMaster(ipAddress, DEFAULT_PORT, DEFAULT_TIMEOUT, DEFAULT_RECONNECT, DEFAULT_RTU_OVER_TCP)
        );
    }

    @Override
    public GenDriverAPI4Modbus createTcpTransport(String ipAddress, int port) {
        return new J2ModModbusClient<>(
            new ModbusTCPMaster(ipAddress, port, DEFAULT_TIMEOUT, DEFAULT_RECONNECT, DEFAULT_RTU_OVER_TCP)
        );
    }

    @Override
    public GenDriverAPI4Modbus createUdpTransport(String ipAddress) {
        return new J2ModModbusClient<>(
            new ModbusUDPMaster(ipAddress, DEFAULT_PORT)
        );
    }

    @Override
    public GenDriverAPI4Modbus createUdpTransport(String ipAddress, int port) {
        return new J2ModModbusClient<>(
            new ModbusUDPMaster(ipAddress, port)
        );
    }

    private static SerialParameters getSerialParameters(String portName, int baudRate, Parity parity, DataBits dataBits, StopBits stopBits) {
		return new SerialParameters(
			portName,
			baudRate,
			AbstractSerialConnection.FLOW_CONTROL_DISABLED,
			AbstractSerialConnection.FLOW_CONTROL_DISABLED,
			DatabitMapper.map(dataBits),
			StopbitMapper.map(stopBits),
			ParityMapper.map(parity),
			DEFAULT_ECHO
		);
	}

    private static SerialParameters getSerialParameters(String portName, int baudRate, Parity parity, DataBits dataBits) {
        return getSerialParameters(portName, baudRate, parity, dataBits, StopBits.ONE);
    }

    private static SerialParameters getSerialParameters(String portName, int baudRate, Parity parity) {
        return getSerialParameters(portName, baudRate, parity, DataBits.EIGHT, StopBits.ONE);
    }

    private static SerialParameters getSerialParameters(String portName, int baudRate) {
        return getSerialParameters(portName, baudRate, Parity.NONE, DataBits.EIGHT, StopBits.ONE);
    }

    private static SerialParameters getSerialParameters(String portName) {
        return getSerialParameters(portName, DEFAULT_BAUD, Parity.NONE, DataBits.EIGHT, StopBits.ONE);
    }
}
