package de.re.easymodbus.adapter;
/*
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

import com.smartgridready.driver.api.common.GenDriverException;
import com.smartgridready.driver.api.modbus.GenDriverModbusException;
import com.smartgridready.driver.api.modbus.GenDriverSocketException;

import de.re.easymodbus.exceptions.ConnectionException;
import de.re.easymodbus.exceptions.FunctionCodeNotSupportedException;
import de.re.easymodbus.exceptions.ModbusException;
import de.re.easymodbus.exceptions.QuantityInvalidException;
import de.re.easymodbus.exceptions.StartingAddressInvalidException;
import de.re.easymodbus.modbusclient.ModbusClient;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements a Modbus command handler.
 * @param <T> the address type
 * @param <U> the parameter type
 * @param <R> the result type
 */
public class ModbusCallHandler<T, U, R> {

    private static final Logger LOG = LoggerFactory.getLogger(ModbusCallHandler.class);

    private static final int MAX_RETRY = 1;

    private final ModbusClient modbusClient;
    private final ModbusReadFunction<T, U, R> readFunction;
    private final ModbusWriteFunction<T, U> writeFunction;
    private final ModbusConnectFunctionTCP connectFunction;

    private int retryCredit = MAX_RETRY;

    /**
     * Defines the interface of a Modbus read function.
     * @param <T> the first parameter type, usually the starting address
     * @param <U> the second parameter type, usually a counter
     * @param <R> the return type
     */
    @FunctionalInterface
    public interface ModbusReadFunction<T, U, R> {

        /**
         * Executes the function.
         * @param t the first parameter, usually the starting address
         * @param u the second parameter, usually a counter
         * @return the read data
         * @throws ModbusException when a protocol error occurred
         * @throws IOException when an I/O error occurred
         * @throws SerialPortException when a serial connection error occurred
         * @throws SerialPortTimeoutException when the serial connection timed out
         */
        R read(T t, U u) throws ModbusException, IOException, SerialPortException, SerialPortTimeoutException;
    }

    /**
     * Defines the interface of a Modbus write function.
     * @param <T> the first parameter type, usually the starting address
     * @param <U> the second parameter type, usually the values to write
     */
    @FunctionalInterface
    public interface ModbusWriteFunction<T, U> {

        /**
         * Executes the function.
         * @param t the first parameter, usually the starting address
         * @param u the second parameter, usually the values to write
         * @throws ModbusException when a protocol error occurred
         * @throws IOException when an I/O error occurred
         * @throws SerialPortException when a serial connection error occurred
         * @throws SerialPortTimeoutException when the serial connection timed out
         */
        void write(T t, U u) throws ModbusException, IOException, SerialPortException, SerialPortTimeoutException;
    }

    /**
     * Defines the interface of a connect function.
     */
    @FunctionalInterface
    public interface ModbusConnectFunctionTCP {

        /**
         * Executes the function.
         * @param ipAddress the IP address
         * @param port the TCP port
         * @throws IOException when the connection failed
         */
        void apply(String ipAddress, int port) throws IOException;
    }

    /**
     * Construct a read command handler.
     * @param modbusClient the Modbus client
     * @param readFunction the read function
     */
    public ModbusCallHandler(
            ModbusClient modbusClient,
            ModbusReadFunction<T, U, R> readFunction) {
        this.modbusClient = modbusClient;
        this.readFunction = readFunction;
        this.writeFunction = null;
        this.connectFunction = null;
    }

    /**
     * Construct a write command handler.
     * @param modbusClient the Modbus client
     * @param writeFunction the write function
     */
    public ModbusCallHandler(
            ModbusClient modbusClient,
            ModbusWriteFunction<T, U> writeFunction) {
        this.modbusClient = modbusClient;
        this.readFunction = null;
        this.writeFunction = writeFunction;
        this.connectFunction = null;
    }

    /**
     * Construct a connect command handler.
     * @param modbusClient the Modbus client
     * @param readFunction the read function
     * @param connectFunction the connect function
     */
    public ModbusCallHandler(
            ModbusClient modbusClient,
            ModbusReadFunction<T, U, R> readFunction,
            ModbusConnectFunctionTCP connectFunction) {
        this.modbusClient = modbusClient;
        this.readFunction = readFunction;
        this.writeFunction = null;
        this.connectFunction = connectFunction;
    }

    /**
     * Construct a connect command handler.
     * @param modbusClient the Modbus client
     * @param writeFunction the write function
     * @param connectFunction the connect function
     */
    public ModbusCallHandler(
            ModbusClient modbusClient,
            ModbusWriteFunction<T, U> writeFunction,
            ModbusConnectFunctionTCP connectFunction) {
        this.modbusClient = modbusClient;
        this.readFunction = null;
        this.writeFunction = writeFunction;
        this.connectFunction = connectFunction;
    }

    /**
     * Performs a read command.
     * @param address the starting address
     * @param parameter the command parameter
     * @return a read result
     * @throws GenDriverException when a general error occurred
     * @throws GenDriverSocketException when a network error occurred
     * @throws GenDriverModbusException when a protocol error occurred
     */
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
        } catch (FunctionCodeNotSupportedException e) {
            throw new GenDriverModbusException(errorReport(msgTemplate, e), e, 0x01);
        } catch (StartingAddressInvalidException e) {
            throw new GenDriverModbusException(errorReport(msgTemplate, e), e, 0x02);
        } catch (QuantityInvalidException e) {
            throw new GenDriverModbusException(errorReport(msgTemplate, e), e, 0x03);
        } catch (ConnectionException e) {
            throw new GenDriverSocketException(errorReport(msgTemplate, e), e);
        } catch (ModbusException e) {
            throw new GenDriverModbusException(errorReport(msgTemplate, e), e, 0x04);
        } catch (IOException | SerialPortException | SerialPortTimeoutException e) {
            throw new GenDriverException(errorReport(msgTemplate, e), e);
        }
    }

    /**
     * Performs a write command.
     * @param address the starting address
     * @param parameter the command parameter
     * @throws GenDriverException when a general error occurred
     * @throws GenDriverSocketException when a network error occurred
     * @throws GenDriverModbusException when a protocol error occurred
     */
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
        } catch (FunctionCodeNotSupportedException e) {
            throw new GenDriverModbusException(errorReport(msgTemplate, e), e, 0x01);
        } catch (StartingAddressInvalidException e) {
            throw new GenDriverModbusException(errorReport(msgTemplate, e), e, 0x02);
        } catch (QuantityInvalidException e) {
            throw new GenDriverModbusException(errorReport(msgTemplate, e), e, 0x03);
        } catch (ConnectionException e) {
            throw new GenDriverSocketException(errorReport(msgTemplate, e), e);
        } catch (ModbusException e) {
            throw new GenDriverModbusException(errorReport(msgTemplate, e), e, 0x04);
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
        LOG.warn(msg);
        return msg;
    }
}
