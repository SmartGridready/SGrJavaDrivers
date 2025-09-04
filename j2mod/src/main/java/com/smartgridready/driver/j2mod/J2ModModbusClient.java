package com.smartgridready.driver.j2mod;

import com.smartgridready.driver.api.modbus.GenDriverAPI4Modbus;
import com.ghgande.j2mod.modbus.ModbusException;
import com.ghgande.j2mod.modbus.facade.AbstractModbusMaster;
import com.ghgande.j2mod.modbus.procimg.InputRegister;
import com.ghgande.j2mod.modbus.procimg.Register;
import com.ghgande.j2mod.modbus.procimg.SimpleRegister;
import com.ghgande.j2mod.modbus.util.BitVector;
import com.smartgridready.driver.api.common.GenDriverException;
import com.smartgridready.driver.api.modbus.GenDriverModbusException;
import com.smartgridready.driver.api.modbus.GenDriverSocketException;

/**
 * Implements a Modbus interface driver based on j2mod.
 * @param <T> the type of Modbus master
 */
public class J2ModModbusClient<T extends AbstractModbusMaster> implements GenDriverAPI4Modbus {

    private final T mbDevice;
    private short unitId;

    /**
     * Construct.
     * @param mbDevice the Modbus master instance
     */
    public J2ModModbusClient(T mbDevice) {
        this.mbDevice = mbDevice;
        this.unitId = 0;
    }

    /**
     * Sets the Modbus unit identifier / slave ID for future commands.
     * This method is deprecated, as the current implementation prefers sending the unit ID with each command.
     * @param unitId the new unit ID
     */
    @Override
    public void setUnitIdentifier(short unitId) {
        this.unitId = unitId;
    }

    /**
     * Reads one or multiple holding registers.
     * This method is deprecated, as the current implementation prefers sending the unit ID with each command. Use {@code readHoldingRegisters} instead.
     * @param startingAddress the first register address to read
     * @param quantity the number of registers to read
     * @return an array of integers
     * @throws GenDriverException when a general error occurred
     * @throws GenDriverSocketException when a network error occurred
     * @throws GenDriverModbusException when a Modbus protocol error occurred
     */
    @Override
    public int[] ReadHoldingRegisters(int startingAddress, int quantity)
            throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
        return readHoldingRegisters(unitId, startingAddress, quantity);
    }

    /**
     * Reads one or multiple input registers.
     * This method is deprecated, as the current implementation prefers sending the unit ID with each command. Use {@code readInputRegisters} instead.
     * @param startingAddress the first register address to read
     * @param quantity the number of registers to read
     * @return an array of integers
     * @throws GenDriverException when a general error occurred
     * @throws GenDriverSocketException when a network error occurred
     * @throws GenDriverModbusException when a Modbus protocol error occurred
     */
    @Override
    public int[] ReadInputRegisters(int startingAddress, int quantity)
            throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
        return readInputRegisters(unitId, startingAddress, quantity);
    }

    /**
     * Reads one or multiple discrete inputs.
     * This method is deprecated, as the current implementation prefers sending the unit ID with each command. Use {@code readDiscreteInputs} instead.
     * @param startingAddress the first discrete input address to read
     * @param quantity the number of discrete inputs to read
     * @return an array of boolean
     * @throws GenDriverException when a general error occurred
     * @throws GenDriverSocketException when a network error occurred
     * @throws GenDriverModbusException when a Modbus protocol error occurred
     */
    @Override
    public boolean[] ReadDiscreteInputs(int startingAddress, int quantity)
            throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
        return readDiscreteInputs(unitId, startingAddress, quantity);
    }

    /**
     * Reads one or multiple coils.
     * This method is deprecated, as the current implementation prefers sending the unit ID with each command. Use {@code readCoils} instead.
     * @param startingAddress the first coil address to read
     * @param quantity the number of coils to read
     * @return an array of boolean
     * @throws GenDriverException when a general error occurred
     * @throws GenDriverSocketException when a network error occurred
     * @throws GenDriverModbusException when a Modbus protocol error occurred
     */
    @Override
    public boolean[] ReadCoils(int startingAddress, int quantity)
            throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
        return readCoils(unitId, startingAddress, quantity);
    }

    /**
     * Writes multiple coils.
     * This method is deprecated, as the current implementation prefers sending the unit ID with each command. Use {@code writeMultipleCoils} instead.
     * @param startingAddress the first coil address to write
     * @param values the array of coil values to write
     * @throws GenDriverException when a general error occurred
     * @throws GenDriverSocketException when a network error occurred
     * @throws GenDriverModbusException when a Modbus protocol error occurred
     */
    @Override
    public void WriteMultipleCoils(int startingAddress, boolean[] values)
            throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
        writeMultipleCoils(unitId, startingAddress, values);
    }

    /**
     * Writes a single coil.
     * This method is deprecated, as the current implementation prefers sending the unit ID with each command. Use {@code writeSingleCoil} instead.
     * @param startingAddress the coil address to write
     * @param value the coil value to write
     * @throws GenDriverException when a general error occurred
     * @throws GenDriverSocketException when a network error occurred
     * @throws GenDriverModbusException when a Modbus protocol error occurred
     */
    @Override
    public void WriteSingleCoil(int startingAddress, boolean value)
            throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
        writeSingleCoil(unitId, startingAddress, value);
    }

    /**
     * Writes multiple holding registers.
     * This method is deprecated, as the current implementation prefers sending the unit ID with each command. Use {@code writeMultipleRegisters} instead.
     * @param startingAddress the first register address to write
     * @param values the array of register values to write
     * @throws GenDriverException when a general error occurred
     * @throws GenDriverSocketException when a network error occurred
     * @throws GenDriverModbusException when a Modbus protocol error occurred
     */
    @Override
    public void WriteMultipleRegisters(int startingAddress, int[] values)
            throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
        writeMultipleRegisters(unitId, startingAddress, values);
    }

    /**
     * Writes a single holding register.
     * This method is deprecated, as the current implementation prefers sending the unit ID with each command. Use {@code writeSingleRegister} instead.
     * @param startingAddress the register address to write
     * @param value the register value to write
     * @throws GenDriverException when a general error occurred
     * @throws GenDriverSocketException when a network error occurred
     * @throws GenDriverModbusException when a Modbus protocol error occurred
     */
    @Override
    public void WriteSingleRegister(int startingAddress, int value)
            throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
        writeSingleRegister(unitId, startingAddress, value);
    }

    /**
     * Reads one or multiple holding registers.
     * @param unitId the unit identifier
     * @param startingAddress the first register address to read
     * @param quantity the number of registers to read
     * @return an array of integers
     * @throws GenDriverException when a general error occurred
     * @throws GenDriverSocketException when a network error occurred
     * @throws GenDriverModbusException when a Modbus protocol error occurred
     */
    @Override
    public int[] readHoldingRegisters(short unitId, int startingAddress, int quantity)
            throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
        try {
            Register[] res = mbDevice.readMultipleRegisters(unitId, startingAddress, quantity);
            return convertRegistersToValues(res);
        } catch (ModbusException e) {
            throw new GenDriverModbusException("Error reading holding registers", e);
        }
    }

    /**
     * Reads one or multiple input registers.
     * @param unitId the unit identifier
     * @param startingAddress the first register address to read
     * @param quantity the number of registers to read
     * @return an array of integers
     * @throws GenDriverException when a general error occurred
     * @throws GenDriverSocketException when a network error occurred
     * @throws GenDriverModbusException when a Modbus protocol error occurred
     */
    @Override
    public int[] readInputRegisters(short unitId, int startingAddress, int quantity)
            throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
        try {
            InputRegister[] res = mbDevice.readInputRegisters(unitId, startingAddress, quantity);
            return convertRegistersToValues(res);
        } catch (ModbusException e) {
            throw new GenDriverModbusException("Error reading input registers", e);
        }
    }

    /**
     * Reads one or multiple discrete inputs.
     * @param unitId the unit identifier
     * @param startingAddress the first discrete input address to read
     * @param quantity the number of discrete inputs to read
     * @return an array of boolean
     * @throws GenDriverException when a general error occurred
     * @throws GenDriverSocketException when a network error occurred
     * @throws GenDriverModbusException when a Modbus protocol error occurred
     */
    @Override
    public boolean[] readDiscreteInputs(short unitId, int startingAddress, int quantity)
            throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
        try {
            BitVector res = mbDevice.readInputDiscretes(unitId, startingAddress, quantity);
            return convertBitVectorToValues(res);
        } catch (ModbusException e) {
            throw new GenDriverModbusException("Error reading discrete inputs", e);
        }
    }

    /**
     * Reads one or multiple coils.
     * @param unitId the unit identifier
     * @param startingAddress the first coil address to read
     * @param quantity the number of coils to read
     * @return an array of boolean
     * @throws GenDriverException when a general error occurred
     * @throws GenDriverSocketException when a network error occurred
     * @throws GenDriverModbusException when a Modbus protocol error occurred
     */
    @Override
    public boolean[] readCoils(short unitId, int startingAddress, int quantity)
            throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
        try {
            BitVector res = mbDevice.readCoils(unitId, startingAddress, quantity);
            return convertBitVectorToValues(res);
        } catch (ModbusException e) {
            throw new GenDriverModbusException("Error reading coils", e);
        }
    }

    /**
     * Writes multiple coils.
     * @param unitId the unit identifier
     * @param startingAddress the first coil address to write
     * @param values the array of coil values to write
     * @throws GenDriverException when a general error occurred
     * @throws GenDriverSocketException when a network error occurred
     * @throws GenDriverModbusException when a Modbus protocol error occurred
     */
    @Override
    public void writeMultipleCoils(short unitId, int startingAddress, boolean[] values)
            throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
        try {
            BitVector bv = convertValuesToBitVector(values);
            mbDevice.writeMultipleCoils(unitId, bv);
        } catch (ModbusException e) {
            throw new GenDriverModbusException("Error writing coils", e);
        }
    }

    /**
     * Writes a single coil.
     * @param unitId the unit identifier
     * @param startingAddress the coil address to write
     * @param value the coil value to write
     * @throws GenDriverException when a general error occurred
     * @throws GenDriverSocketException when a network error occurred
     * @throws GenDriverModbusException when a Modbus protocol error occurred
     */
    @Override
    public void writeSingleCoil(short unitId, int startingAddress, boolean value)
            throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
        try {
            mbDevice.writeCoil(unitId, startingAddress, value);
        } catch (ModbusException e) {
            throw new GenDriverModbusException("Error writing coil", e);
        }
    }

    /**
     * Writes multiple holding registers.
     * @param unitId the unit identifier
     * @param startingAddress the first register address to write
     * @param values the array of register values to write
     * @throws GenDriverException when a general error occurred
     * @throws GenDriverSocketException when a network error occurred
     * @throws GenDriverModbusException when a Modbus protocol error occurred
     */
    @Override
    public void writeMultipleRegisters(short unitId, int startingAddress, int[] values)
            throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
        try {
            Register[] registers = convertValuesToRegisters(values);
            mbDevice.writeMultipleRegisters(unitId, startingAddress, registers);
        } catch (ModbusException e) {
            throw new GenDriverModbusException("Error writing registers", e);
        }
    }

    /**
     * Writes a single holding register.
     * @param unitId the unit identifier
     * @param startingAddress the register address to write
     * @param value the register value to write
     * @throws GenDriverException when a general error occurred
     * @throws GenDriverSocketException when a network error occurred
     * @throws GenDriverModbusException when a Modbus protocol error occurred
     */
    @Override
    public void writeSingleRegister(short unitId, int startingAddress, int value)
            throws GenDriverException, GenDriverSocketException, GenDriverModbusException {
        try {
            mbDevice.writeSingleRegister(unitId, startingAddress, new SimpleRegister(value));
        } catch (ModbusException e) {
            throw new GenDriverModbusException("Error writing register", e);
        }
    }

    /**
     * Connects to the Modbus interface. 
     * @return true if connected, false otherwise
     * @throws GenDriverException when connection failed
     */
    @Override
    public boolean connect() throws GenDriverException {
        try {
            mbDevice.connect();
            return mbDevice.isConnected();
        } catch (Exception e) {
            throw new GenDriverException("Connect failed.", e);
        }
    }

    /**
     * Disconnects from the Modbus interface.
     * @throws GenDriverException when an error occurred
     */
    @Override
    public void disconnect() throws GenDriverException {
        try {
            mbDevice.disconnect();
        } catch (Exception e) {
            throw new GenDriverException("Disconnect failed.", e);
        }
    }

    /**
     * Tells if the Modbus interface is connected.
     * @return true if connected, false otherwise
     */
    @Override
    public boolean isConnected() {
        return mbDevice.isConnected();
    }

    /**
     * Converts registers to integer values.
     * @param <T> the register type
     * @param registers an array of registers
     * @return an array of int
     */
    protected static <T extends InputRegister> int[] convertRegistersToValues(T[] registers) {
        int[] values = new int[registers.length];
        for (int i = 0; i < registers.length; i++) {
            values[i] = registers[i].getValue();
        }
        return values;
    }

    /**
     * Converts integer values to registers.
     * @param values an array of int
     * @return an array of {@link Register}
     */
    protected static Register[] convertValuesToRegisters(int[] values) {
        Register[] registers = new Register[values.length];
        for (int i = 0; i < values.length; i++) {
            registers[i] = new SimpleRegister(values[i]);
        }
        return registers;
    }

    /**
     * Converts a bit vector to boolean values.
     * @param bv the bit vector
     * @return an array of boolean
     */
    protected static boolean[] convertBitVectorToValues(BitVector bv) {
        int n = bv.size();
        boolean[] values = new boolean[n];
        for (int i = 0; i < n; i++) {
            values[i] = bv.getBit(i);
        }
        return values;
    }

    /**
     * Converts boolean values to a bit vector.
     * @param values an array of boolean
     * @return an instance of {@link BitVector}
     */
    protected static BitVector convertValuesToBitVector(boolean[] values) {
        int n = values.length;
        BitVector bv = new BitVector(n);
        for (int i = 0; i < n; i++) {
            bv.setBit(i, values[i]);
        }
        return bv;
    }
}
