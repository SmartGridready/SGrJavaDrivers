package com.smartgridready.driver.j2mod;

import com.smartgridready.driver.api.modbus.DataBits;
import com.smartgridready.driver.api.modbus.GenDriverAPI4Modbus;
import com.smartgridready.driver.api.modbus.GenDriverAPI4ModbusFactory;
import com.smartgridready.driver.api.modbus.Parity;
import com.smartgridready.driver.api.modbus.StopBits;

public class J2ModModbusClientFactory implements GenDriverAPI4ModbusFactory {
    
    @Override
    public GenDriverAPI4Modbus createRtuTransport(String comPort) {
        return new J2ModModbusClientRtu(comPort);
    }

    @Override
    public GenDriverAPI4Modbus createRtuTransport(String comPort, int baudRate) {
        return new J2ModModbusClientRtu(comPort, baudRate);
    }

    @Override
    public GenDriverAPI4Modbus createRtuTransport(String comPort, int baudRate, Parity parity) {
        return new J2ModModbusClientRtu(comPort, baudRate, parity);
    }

    @Override
    public GenDriverAPI4Modbus createRtuTransport(String comPort, int baudRate, Parity parity, DataBits dataBits) {
        return new J2ModModbusClientRtu(comPort, baudRate, parity, dataBits);
    }

    @Override
    public GenDriverAPI4Modbus createRtuTransport(String comPort, int baudRate, Parity parity, DataBits dataBits, StopBits stopBits) {
        return new J2ModModbusClientRtu(comPort, baudRate, parity, dataBits, stopBits);
    }

    @Override
    public GenDriverAPI4Modbus createTcpTransport(String ipAddress) {
        return new J2ModModbusClientTcp(ipAddress);
    }

    @Override
    public GenDriverAPI4Modbus createTcpTransport(String ipAddress, int port) {
        return new J2ModModbusClientTcp(ipAddress, port);
    }

    @Override
    public GenDriverAPI4Modbus createUdpTransport(String ipAddress) {
        return new J2ModModbusClientUdp(ipAddress);
    }

    @Override
    public GenDriverAPI4Modbus createUdpTransport(String ipAddress, int port) {
        return new J2ModModbusClientUdp(ipAddress, port);
    }
}
