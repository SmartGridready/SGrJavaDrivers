package com.smartgridready.driver.j2mod;

import com.ghgande.j2mod.modbus.facade.ModbusSerialMaster;
import com.ghgande.j2mod.modbus.net.AbstractSerialConnection;
import com.ghgande.j2mod.modbus.util.SerialParameters;
import com.smartgridready.driver.api.modbus.DataBits;
import com.smartgridready.driver.api.modbus.Parity;
import com.smartgridready.driver.api.modbus.StopBits;
import com.smartgridready.driver.j2mod.util.DatabitMapper;
import com.smartgridready.driver.j2mod.util.ParityMapper;
import com.smartgridready.driver.j2mod.util.StopbitMapper;

public class J2ModModbusClientRtu extends J2ModModbusClientBase {

	public J2ModModbusClientRtu(String portName, int baudRate, Parity parity, DataBits dataBits, StopBits stopBits) {
		super(new ModbusSerialMaster(getSerialParameters(portName, baudRate, parity, dataBits, stopBits)));
	}

	public J2ModModbusClientRtu(String portName, int baudRate, Parity parity, DataBits dataBits) {
		this(portName, baudRate, parity, dataBits, StopBits.ONE);
	}

	public J2ModModbusClientRtu(String portName, int baudRate, Parity parity) {
		this(portName, baudRate, parity, DataBits.EIGHT, StopBits.ONE);
	}

	public J2ModModbusClientRtu(String portName, int baudRate) {
		this(portName, baudRate, Parity.NONE, DataBits.EIGHT, StopBits.ONE);
	}

	public J2ModModbusClientRtu(String portName) {
		this(portName, 9600, Parity.NONE, DataBits.EIGHT, StopBits.ONE);
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
			false
		);
	}
}
