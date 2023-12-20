package de.re.easymodbus.adapter;

/**
*Copyright(c) 2021 Verein SmartGridready Switzerland
* @generated NOT
* 
This Open Source Software is BSD 3 clause licensed:
Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in 
   the documentation and/or other materials provided with the distribution.
3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from 
   this software without specific prior written permission.
THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR 
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, 
PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED 
OF THE POSSIBILITY OF SUCH DAMAGE.

It includes completely manually generated code. It is the Interface of the Modbus RTU inside the CommHandler and its purpose is to enable 3rd party programmers to
use their own Modbus TCP drivers
 */

import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Logger;

import communicator.common.runtime.GenDriverException;
import communicator.common.runtime.GenDriverModbusException;
import communicator.common.runtime.GenDriverSocketException;
import de.re.easymodbus.exceptions.ModbusException;
import de.re.easymodbus.modbusclient.ModbusClient;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

public class ModbusCallHandler<T, U, R> {

	private static Logger LOG = Logger.getLogger(ModbusCallHandler.class.getName());

	private static final int MAX_RETRY = 1;

	private final ModbusClient modbusClient;
	private final ModbusReadFunction<T, U, R> readFunction;
	private final ModbusWriteFunction<T, U> writeFunction;
	private final ModbusConnectFunctionTCP connectFunction;

	private int retryCredit = MAX_RETRY;

	@FunctionalInterface
	public interface ModbusReadFunction<T, U, R> {
		R read(T t, U u) throws ModbusException, IOException, SerialPortException, SerialPortTimeoutException;
	}

	@FunctionalInterface
	public interface ModbusWriteFunction<T, U> {
		void write(T t, U u) throws ModbusException, IOException, SerialPortException, SerialPortTimeoutException;
	}

	@FunctionalInterface
	public interface ModbusConnectFunctionTCP {
		void apply(String ipAddress, int port) throws IOException;
	}

	public ModbusCallHandler(
			ModbusClient modbusClient,
			ModbusReadFunction<T, U, R> readFunction) {
		this.modbusClient = modbusClient;
		this.readFunction = readFunction;
		this.writeFunction = null;
		this.connectFunction = null;
	}

	public ModbusCallHandler(
			ModbusClient modbusClient,
			ModbusWriteFunction<T, U> writeFunction) {
		this.modbusClient = modbusClient;
		this.readFunction = null;
		this.writeFunction = writeFunction;
		this.connectFunction = null;
	}

	public ModbusCallHandler(
			ModbusClient modbusClient,
			ModbusReadFunction<T, U, R> readFunction,
			ModbusConnectFunctionTCP connectFunction) {
		this.modbusClient = modbusClient;
		this.readFunction = readFunction;
		this.writeFunction = null;
		this.connectFunction = connectFunction;
	}

	public ModbusCallHandler(
			ModbusClient modbusClient,
			ModbusWriteFunction<T, U> writeFunction,
			ModbusConnectFunctionTCP connectFunction) {
		this.modbusClient = modbusClient;
		this.readFunction = null;
		this.writeFunction = writeFunction;
		this.connectFunction = connectFunction;
	}

	public R read(T address, U parameter)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		String msgTemplate = "Modbus read error: %s";

		try {
			return readFunction.read(address, parameter);
		} catch (SocketException e) {
			if ((connectFunction == null) || (retryCredit <= 0)) {
				throw new GenDriverSocketException(errorReport(msgTemplate, e), e);
			} else {
				retryCredit--;
				return retryRead(address, parameter);
			}
		} catch (ModbusException e) {
			throw new GenDriverModbusException(errorReport(msgTemplate, e), e);
		} catch (IOException | SerialPortException | SerialPortTimeoutException e) {
			throw new GenDriverException(errorReport(msgTemplate, e), e);
		}
	}

	public void write(T address, U parameter)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {

		String msgTemplate = "Modbus write error: %s";

		try {
			writeFunction.write(address, parameter);
		} catch (SocketException e) {
			if ((connectFunction == null) || (retryCredit <= 0)) {
				throw new GenDriverSocketException(errorReport(msgTemplate, e), e);
			} else {
				retryCredit--;
				retryWrite(address, parameter);
			}
		} catch (ModbusException e) {
			throw new GenDriverModbusException(errorReport(msgTemplate, e), e);
		} catch (IOException | SerialPortException | SerialPortTimeoutException e) {
			throw new GenDriverException(errorReport(msgTemplate, e), e);
		}
	}

	private R retryRead(T startingAddress, U parameter)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		LOG.info("Reconnecting and retrying modbus read.");
		tryConnect();
		R result = read(startingAddress, parameter);
		LOG.info("Reconnect and retry modbus write succeeded.");
		return result;
	}

	private void retryWrite(T startingAddress, U parameter)
			throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
		LOG.info("Reconnecting and retrying modbus write.");
		tryConnect();
		write(startingAddress, parameter);
		LOG.info("Reconnect and retry modbus write succeeded.");
	}

	private void tryConnect() throws GenDriverException {

		try {
			connectFunction.apply(modbusClient.getipAddress(), modbusClient.getPort());
		} catch (IOException e) {
			throw new GenDriverException("Retry: Unable to re-connect to ipAddr=" + modbusClient.getipAddress()
					+ " port=" + modbusClient.getPort(), e);
		}
	}

	private String errorReport(String messageTemplate, Exception e) {
		String msg = String.format(messageTemplate, e.getMessage());
		LOG.warning(msg);
		return msg;
	}
}
