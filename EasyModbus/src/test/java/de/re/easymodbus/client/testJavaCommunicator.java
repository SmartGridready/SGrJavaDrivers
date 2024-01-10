/**
 * @author Chris Broennimann
 *
 */

package de.re.easymodbus.client;

import java.io.IOException;

import de.re.easymodbus.datatypes.Parity;
import de.re.easymodbus.datatypes.StopBits;
import de.re.easymodbus.exceptions.ModbusException;
import de.re.easymodbus.modbusclient.ModbusClient;
//import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;


public class testJavaCommunicator {

	/**
	 * @param args
	 */

    public static void main(String args[]) throws IOException , SerialPortException, ModbusException, SerialPortTimeoutException, InterruptedException
    {
    	System.out.println("starting sample communication handler for the JAVA based alpha test of the SmartGridready appplication layer interoparability solution ....");

    	// Reading Modbus device definitions
    	// ABB SmartMeter
    	// starting the communication handler
    	
    	
    	/////////////////////////////////////////////////////////////////////
    	// Modbus tcp clients
    	/////////////////////////////////////////////////////////////////////
    	ModbusClient mbGaroWallbox = new ModbusClient("192.168.1.182",502);
    	ModbusClient mbFroniusRoofInverter = new ModbusClient("192.168.1.181",502); 
   	    boolean enough = false;
   	    int loopcount = 0;
   	   int[] responseint;
   	 
   	    while (enough==false)
   	    {
          try
   	      {
   	          // TCP/IP connection aufstarten 
   	    	  if (loopcount++ > 5) enough = true;
     	      mbGaroWallbox.Connect();
  	    	  responseint = mbGaroWallbox.ReadHoldingRegisters(212, 2);   	    	 
              System.out.println("TCP GaroWallbox I(L1)=" +   (((responseint[0]*65536) + responseint[1])/1000.0)+" A");
  	    	  responseint = mbGaroWallbox.ReadHoldingRegisters(214, 2);   	    
              System.out.println("TCP GaroWallbox I(L2)=" +   (((responseint[0]*65536) + responseint[1])/1000.0)+" A");
  	    	  responseint = mbGaroWallbox.ReadHoldingRegisters(216, 2);   	    
              System.out.println("TCP GaroWallbox I(L3)=" +   (((responseint[0]*65536) + responseint[1])/1000.0)+" A");
      	      //mbGaroWallbox.Disconnect();
  	          Thread.sleep(1000); 
  	          
     	      mbFroniusRoofInverter.Connect();
  	    	  responseint = mbFroniusRoofInverter.ReadHoldingRegisters(40072, 3);   	    	 
              System.out.println("TCP FroniusRoofInverter I(L1)=" +  (responseint[0]/100.0)+" A");  
              System.out.println("TCP FroniusRoofInverter I(L2)=" +  (responseint[1]/100.0)+" A");	    
              System.out.println("TCP FroniusRoofInverter I(L3)=" +  (responseint[2]/100.0)+" A");

  	    	  responseint = mbFroniusRoofInverter.ReadHoldingRegisters(40072, 1);   	    	 
              System.out.println("TCP FroniusRoofInverter I(L1)=" +  (responseint[0]/100.0)+" A");  
  	    	  responseint = mbFroniusRoofInverter.ReadHoldingRegisters(40073, 1);   	
              System.out.println("TCP FroniusRoofInverter I(L2)=" +  (responseint[1]/100.0)+" A");	    
  	    	  responseint = mbFroniusRoofInverter.ReadHoldingRegisters(40074, 1);   	
              System.out.println("TCP FroniusRoofInverter I(L3)=" +  (responseint[2]/100.0)+" A");
              
         	  //mbFroniusRoofInverter.Disconnect();
  	          Thread.sleep(1000);  
  	          
   	      }
   	       
          catch (Exception e)
          {
        	  e.printStackTrace();
          }
          finally
          {
        	  mbGaroWallbox.Disconnect();
          }
   	    }  
   	    if (loopcount > 10) System.out.println("TCP test failed, check connection");
        else System.out.println("TCP test with Garo Wallbox completed");
   	    
   	    
    	
    	/////////////////////////////////////////////////////////////////////
    	// Modbus RTU Units
    	/////////////////////////////////////////////////////////////////////
   	    System.out.println("starting RTU test:");
   	    ModbusClient mbRTU = new ModbusClient();
   	    loopcount = 0; 
   	    enough = false;
        try 
        {          
           mbRTU.setSerialFlag(true);
           mbRTU.setSerialPort("COM6");
       	   mbRTU.setBaudrate(19200);
       	   mbRTU.setParity(Parity.Even);
       	   mbRTU.setStopBits(StopBits.One);
       	   //mbRTU.setLogFileName("easyModebusRTULogger.txt");   // der Logger l�uft, wirft aber eine exception
           mbRTU.Connect("COM6");
           Thread.sleep(1000); 
           System.out.println("Modbus Client opened");

      	   while (enough==false)
       	   {
   	    	 if (loopcount++ > 10) enough = true;
           /*  mbRTU.setUnitIdentifier((byte)16);
 	         responseint = mbRTU.ReadHoldingRegisters(10, 6);
 	         System.out.println("RTU SupercalMeter Time=" +  responseint[0] +"-"+ responseint[1] +"-"+ responseint[2] +" "
 	         + responseint[3] +":"+ responseint[4] +":"+ responseint[5]);
             mbRTU.setUnitIdentifier((byte)16); 
  	         responseint = mbRTU.ReadInputRegisters(822, 4);
  	         System.out.println("RTU SupercalMeter Temp high="+(((responseint[1]*65536) + responseint[0])/100.0)+" C�  low=" + (((responseint[3]*65535) + responseint[2])/100.0)+ " C�");
  	         Thread.sleep(1000);  */
             mbRTU.setUnitIdentifier((byte)11);
 	         responseint = mbRTU.ReadHoldingRegisters(23308,16);
 	         System.out.println("ABBMeter I[L1] = " + (((responseint[0]*65536) +responseint[1])/100.0)+" A"); 
 	         System.out.println("ABBMeter I[L2] = " + (((responseint[2]*65536) + responseint[3])/100.0)+" A"); 
 	         System.out.println("ABBMeter I[L3] = " + (((responseint[4]*65536) + responseint[5])/100.0)+" A"); 
 	         //System.out.println("ABBMeter I[N]  = " + (((responseint[6]*65536) + responseint[7])/100.0)+" A"); 
 	         System.out.println("ABBMeter P[tot]= " + (((responseint[8]*65536) +  Math.abs(responseint[9])/100.0))+" W"); 
 	         System.out.println("ABBMeter P[L1] = " + (((responseint[10]*65536) +  Math.abs(responseint[11]))/100.0)+" W"); 
 	         System.out.println("ABBMeter P[L2] = " + (((responseint[12]*65536) +  Math.abs(responseint[13]))/100.0)+" W"); 
 	         System.out.println("ABBMeter P[L3] = " + (((responseint[14]*65536) +  Math.abs(responseint[15]))/100.0)+" W"); 
  	         Thread.sleep(1000); 
       	   }
   	    }
        
        catch (SerialPortException ex) {
          System.out.println(ex);
        }
        catch (Exception e){
        e.printStackTrace();
        }
        finally
        {	
       	 mbRTU.Disconnect();
        }

    }
    
}
