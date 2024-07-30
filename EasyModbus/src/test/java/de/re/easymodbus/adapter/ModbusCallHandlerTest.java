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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.SocketException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.smartgridready.driver.api.common.GenDriverException;
import com.smartgridready.driver.api.modbus.GenDriverModbusException;
import com.smartgridready.driver.api.modbus.GenDriverSocketException;
import de.re.easymodbus.exceptions.ModbusException;
import de.re.easymodbus.modbusclient.ModbusClient;

class ModbusCallHandlerTest {
	
	private static final String CONN_IP_ADDR = "192.168.10.2";
	private static final int CONN_PORT = 4099;
	
	private static final int MODBUS_START_ADDR = 0xC000;
	private static final int READ_REG_QUANTITY = 2;
	private static final int[] READ_REG_RETURN = new int[] {0xAA, 0xBB};
	
		
	@Mock
	ModbusClient mbDevice;	
	
    @BeforeEach 
    public void initMocks() {
       MockitoAnnotations.openMocks(this);
    }
	
    @Test
	void readHoldingRegisters_success() throws Exception {
						
		// given
		final int[] holdingRegData = new int[] {0xAA, 0xBB};
		
		when(mbDevice.ReadHoldingRegisters(MODBUS_START_ADDR, READ_REG_QUANTITY)).thenReturn(holdingRegData);
		
		// when
		int[] result = new ModbusCallHandler<>(
				mbDevice,
				mbDevice::ReadHoldingRegisters,
				mbDevice::Connect).read(MODBUS_START_ADDR, READ_REG_QUANTITY);	
		
		// then
		assertArrayEquals(holdingRegData, result);
		verify(mbDevice, times(1)).ReadHoldingRegisters(MODBUS_START_ADDR, READ_REG_QUANTITY);
		verify(mbDevice, never()).Connect(any(), anyInt());
	}
	
	@Test
	void readHoldingRegisters_retry_once_and_then_success() throws Exception {
		
		// given				
		when(mbDevice.getipAddress()).thenReturn(CONN_IP_ADDR);
		when(mbDevice.getPort()).thenReturn(CONN_PORT);
		
		when(mbDevice.ReadHoldingRegisters(MODBUS_START_ADDR, READ_REG_QUANTITY))
		.thenThrow(new SocketException("Connection reset by peer")) // first: 	throws exception
		.thenReturn(READ_REG_RETURN);							    // second:	retry succeeds.
				
		// when
		int[] result = new ModbusCallHandler<>(
				mbDevice,
				mbDevice::ReadHoldingRegisters,
				mbDevice::Connect).read(MODBUS_START_ADDR, READ_REG_QUANTITY);	
		
		// then
		assertArrayEquals(READ_REG_RETURN, result);
		verify(mbDevice,times(1)).Connect(CONN_IP_ADDR, CONN_PORT);
		verify(mbDevice, times(2)).ReadHoldingRegisters(MODBUS_START_ADDR, READ_REG_QUANTITY);

	}
	
	@Test
	void readHoldingRegisters_retry_thows_SocketException() throws Exception {
		
		// given				
		when(mbDevice.getipAddress()).thenReturn(CONN_IP_ADDR);
		when(mbDevice.getPort()).thenReturn(CONN_PORT);
		
		when(mbDevice.ReadHoldingRegisters(MODBUS_START_ADDR, READ_REG_QUANTITY))
		.thenThrow(new SocketException("Connection reset by peer."))   // first: 	throws exception
		.thenThrow(new SocketException("Connection reset by peer."));  // second:	throw again
				
		// when		
		GenDriverSocketException expectedException = assertThrows( GenDriverSocketException.class,
				() -> new ModbusCallHandler<>(
				mbDevice,
				mbDevice::ReadHoldingRegisters,
				mbDevice::Connect).read(MODBUS_START_ADDR, READ_REG_QUANTITY));
		
		// then		
		assertEquals("Modbus read error: Connection reset by peer.", expectedException.getMessage());
		assertEquals("Connection reset by peer.", expectedException.getCause().getMessage());
		verify(mbDevice,times(1)).Connect(CONN_IP_ADDR, CONN_PORT);
		verify(mbDevice, times(2)).ReadHoldingRegisters(MODBUS_START_ADDR, READ_REG_QUANTITY);
	}
	
	@Test
	void readHoldingRegisters_throws_ModbusException() throws Exception {
		
		// given				
		when(mbDevice.getipAddress()).thenReturn(CONN_IP_ADDR);
		when(mbDevice.getPort()).thenReturn(CONN_PORT);
		
		when(mbDevice.ReadHoldingRegisters(MODBUS_START_ADDR, READ_REG_QUANTITY))
		.thenThrow(new ModbusException("Bus address invalid."));
				
		// when		
		GenDriverModbusException expectedException = assertThrows( GenDriverModbusException.class,
				() -> new ModbusCallHandler<>(
				mbDevice,
				mbDevice::ReadHoldingRegisters,
				mbDevice::Connect).read(MODBUS_START_ADDR, READ_REG_QUANTITY));
		
		// then		
		assertEquals("Modbus read error: Bus address invalid.", expectedException.getMessage());
		assertEquals("Bus address invalid.", expectedException.getCause().getMessage());
		verify(mbDevice, never()).Connect(CONN_IP_ADDR, CONN_PORT); // no retry
		verify(mbDevice, times(1)).ReadHoldingRegisters(MODBUS_START_ADDR, READ_REG_QUANTITY);		
	}
	
	@Test
	void readHoldingRegisters_throws_IOException() throws Exception {
		
		// given				
		when(mbDevice.getipAddress()).thenReturn(CONN_IP_ADDR);
		when(mbDevice.getPort()).thenReturn(CONN_PORT);
		
		when(mbDevice.ReadHoldingRegisters(MODBUS_START_ADDR, READ_REG_QUANTITY))
		.thenThrow(new IOException("Device not reachable."));
				
		// when		
		GenDriverException expectedException = assertThrows( GenDriverException.class,
				() -> new ModbusCallHandler<>(
				mbDevice,
				mbDevice::ReadHoldingRegisters,
				mbDevice::Connect).read(MODBUS_START_ADDR, READ_REG_QUANTITY));
		
		// then		
		assertEquals("Modbus read error: Device not reachable.", expectedException.getMessage());
		assertEquals("Device not reachable.", expectedException.getCause().getMessage());
		verify(mbDevice, never()).Connect(CONN_IP_ADDR, CONN_PORT); // no retry
		verify(mbDevice, times(1)).ReadHoldingRegisters(MODBUS_START_ADDR, READ_REG_QUANTITY);		
	}
	
	@Test
	void readHoldingRegisters_socketException_noReconnectFunction() throws Exception {
		
		// given				
		when(mbDevice.getipAddress()).thenReturn(CONN_IP_ADDR);
		when(mbDevice.getPort()).thenReturn(CONN_PORT);
		
		when(mbDevice.ReadHoldingRegisters(MODBUS_START_ADDR, READ_REG_QUANTITY))
		.thenThrow(new SocketException("Connection reset by peer."));
				
		// when		
		GenDriverSocketException expectedException = assertThrows( GenDriverSocketException.class,
				() -> new ModbusCallHandler<>(
				mbDevice,
				mbDevice::ReadHoldingRegisters).read(MODBUS_START_ADDR, READ_REG_QUANTITY));
		
		// then		
		assertEquals("Modbus read error: Connection reset by peer.", expectedException.getMessage());
		assertEquals("Connection reset by peer.", expectedException.getCause().getMessage());
		verify(mbDevice, never()).Connect(CONN_IP_ADDR, CONN_PORT); // no retry
		verify(mbDevice, times(1)).ReadHoldingRegisters(MODBUS_START_ADDR, READ_REG_QUANTITY);		
		
	}
	
	@Test
	void writeMultipleRegisters_success() throws Exception {
		
		// given
		final int[] regData = new int[] {0xAA, 0xBB};	
		
		// when
		new ModbusCallHandler<>(
				mbDevice,
				mbDevice::WriteMultipleRegisters,
				mbDevice::Connect).write(MODBUS_START_ADDR, regData);	
		
		// then
		verify(mbDevice, times(1)).WriteMultipleRegisters(MODBUS_START_ADDR, regData);
		verify(mbDevice, never()).Connect(any(), anyInt());				
	}
	
	@Test 
	void writeMultipleRegisters_retry_once_and_then_success() throws Exception {
		
		// given
		final int[] regData = new int[] {0xAA, 0xBB};
		
		when(mbDevice.getipAddress()).thenReturn(CONN_IP_ADDR);
		when(mbDevice.getPort()).thenReturn(CONN_PORT);
		
		doThrow(new SocketException("Connection reset by peer."))
		.doNothing()
		.when(mbDevice).WriteMultipleRegisters(MODBUS_START_ADDR, regData);

				
		// when
		new ModbusCallHandler<>(
				mbDevice,
				mbDevice::WriteMultipleRegisters,
				mbDevice::Connect).write(MODBUS_START_ADDR, regData);	
		
		// then
		verify(mbDevice,times(1)).Connect(CONN_IP_ADDR, CONN_PORT);
		verify(mbDevice, times(2)).WriteMultipleRegisters(MODBUS_START_ADDR, regData);	
	}
	
	@Test 
	void writeMultipleRegisters_retry_throws_SocketException() throws Exception {
		
		// given
		final int[] regData = new int[] {0xAA, 0xBB};
		
		when(mbDevice.getipAddress()).thenReturn(CONN_IP_ADDR);
		when(mbDevice.getPort()).thenReturn(CONN_PORT);
		
		doThrow(new SocketException("Connection reset by peer."))
		.when(mbDevice).WriteMultipleRegisters(MODBUS_START_ADDR, regData);

				
		// when
		GenDriverSocketException e = assertThrows( GenDriverSocketException.class, 
				() -> new ModbusCallHandler<>(
					mbDevice,
					mbDevice::WriteMultipleRegisters,
					mbDevice::Connect).write(MODBUS_START_ADDR, regData));	
		
		// then
		assertEquals("Modbus write error: Connection reset by peer.", e.getMessage());
		assertEquals("Connection reset by peer.", e.getCause().getMessage());
		verify(mbDevice,times(1)).Connect(CONN_IP_ADDR, CONN_PORT);
		verify(mbDevice, times(2)).WriteMultipleRegisters(MODBUS_START_ADDR, regData);	
	}
	
	@Test 
	void writeMultipleRegisters_retry_throws_ModbusException() throws Exception {
		
		// given
		final int[] regData = new int[] {0xAA, 0xBB};
		
		when(mbDevice.getipAddress()).thenReturn(CONN_IP_ADDR);
		when(mbDevice.getPort()).thenReturn(CONN_PORT);
		
		doThrow(new ModbusException("Bus address invalid."))
		.when(mbDevice).WriteMultipleRegisters(MODBUS_START_ADDR, regData);

				
		// when
		GenDriverModbusException e = assertThrows( GenDriverModbusException.class, 
				() -> new ModbusCallHandler<>(
					mbDevice,
					mbDevice::WriteMultipleRegisters,
					mbDevice::Connect).write(MODBUS_START_ADDR, regData));	
		
		// then
		assertEquals("Modbus write error: Bus address invalid.", e.getMessage());
		assertEquals("Bus address invalid.", e.getCause().getMessage());
		verify(mbDevice, never()).Connect(CONN_IP_ADDR, CONN_PORT);
		verify(mbDevice, times(1)).WriteMultipleRegisters(MODBUS_START_ADDR, regData);	
	}
	
	@Test
	void writeMultipleRegisters_throws_IOException() throws Exception {
		
		// given
		final int[] regData = new int[] {0xAA, 0xBB};
		
		when(mbDevice.getipAddress()).thenReturn(CONN_IP_ADDR);
		when(mbDevice.getPort()).thenReturn(CONN_PORT);
		
		doThrow(new IOException("Device not reachable."))
		.when(mbDevice).WriteMultipleRegisters(MODBUS_START_ADDR, regData);

				
		// when
		GenDriverException e = assertThrows( GenDriverException.class, 
				() -> new ModbusCallHandler<>(
					mbDevice,
					mbDevice::WriteMultipleRegisters,
					mbDevice::Connect).write(MODBUS_START_ADDR, regData));	
		
		// then
		assertEquals("Modbus write error: Device not reachable.", e.getMessage());
		assertEquals("Device not reachable.", e.getCause().getMessage());
		verify(mbDevice, never()).Connect(CONN_IP_ADDR, CONN_PORT);
		verify(mbDevice, times(1)).WriteMultipleRegisters(MODBUS_START_ADDR, regData);		
	}
	
	@Test
	void writeMultipleRegisters_socketException_noReconnectFunction() throws Exception {
		
		// given
		final int[] regData = new int[] {0xAA, 0xBB};
		
		when(mbDevice.getipAddress()).thenReturn(CONN_IP_ADDR);
		when(mbDevice.getPort()).thenReturn(CONN_PORT);
		
		doThrow(new SocketException("Connection reset by peer."))
		.when(mbDevice).WriteMultipleRegisters(MODBUS_START_ADDR, regData);
				
		// when
		GenDriverSocketException e = assertThrows(GenDriverSocketException.class, 
				() -> new ModbusCallHandler<>(
				mbDevice,
				mbDevice::WriteMultipleRegisters).write(MODBUS_START_ADDR, regData));	
		
		// then
		assertEquals("Modbus write error: Connection reset by peer.", e.getMessage());
		assertEquals("Connection reset by peer.", e.getCause().getMessage());
		verify(mbDevice, never()).Connect(CONN_IP_ADDR, CONN_PORT);
		verify(mbDevice, times(1)).WriteMultipleRegisters(MODBUS_START_ADDR, regData);
	}
		
}
