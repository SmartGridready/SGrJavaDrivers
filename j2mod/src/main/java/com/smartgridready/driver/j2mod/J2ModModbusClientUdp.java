package com.smartgridready.driver.j2mod;

import com.ghgande.j2mod.modbus.facade.ModbusUDPMaster;

public class J2ModModbusClientUdp extends J2ModModbusClientBase {

	private static final int TIMEOUT = 5000;
	private static final int UDP_PORT = 502;

	public J2ModModbusClientUdp(String address, int port) {
		super(new ModbusUDPMaster(address, port, TIMEOUT));
	}

	public J2ModModbusClientUdp(String address) {
		this(address, UDP_PORT);
	}
}
