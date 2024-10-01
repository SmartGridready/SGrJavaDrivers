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
use their own Modbus RTU drivers
 */


import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import com.smartgridready.driver.api.modbus.GenDriverAPI4Modbus;

class GenDriverAPI4ModbusTCPTest {
	
	private static final Logger LOG = LogManager.getLogger(GenDriverAPI4ModbusTCPTest.class);
	
	private final int[] EXPECTED_RESPONSE = new int[] {0xAA, 2};
	
	@Test
	void testReadInputRegistersSuccess() throws Exception {
		
		TestSocketServer server = new TestSocketServer();
		server.start();
		
		GenDriverAPI4Modbus driver = new GenDriverAPI4ModbusTCP("127.0.0.1", 9099);
		driver.connect();				
		
		int[] result = driver.ReadHoldingRegisters(EXPECTED_RESPONSE[0], EXPECTED_RESPONSE[1] );
		reportResult(result);
		assertArrayEquals(EXPECTED_RESPONSE, result);
		
		server.disconnect();
	}

	private void reportResult(int[] result) {
		StringBuffer sbuf = new StringBuffer();
		Arrays.stream(result).boxed().forEach( b -> sbuf.append(String.format("%x, ", b)));		
		LOG.info("Successful read register - result: {}", sbuf.toString());
	}
	
}
