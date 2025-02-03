package com.smartgridready.driver.j2mod;

import com.ghgande.j2mod.modbus.facade.ModbusTCPMaster;

public class J2ModModbusClientTcp extends J2ModModbusClientBase {

	private static final int TIMEOUT = 5000;
	private static final boolean RECONNECT = true;
	private static final boolean RTU_OVER_TCP = false;
	private static final int TCP_PORT = 502;

	public J2ModModbusClientTcp(String address, int port) {
		super(new ModbusTCPMaster(address, port, TIMEOUT, RECONNECT, RTU_OVER_TCP));
	}

	public J2ModModbusClientTcp(String address) {
		this(address, TCP_PORT);
	}
}
