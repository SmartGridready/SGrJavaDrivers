# SGrJavaDrivers

## Index
[Summary](#summary)<br>
[Project Setup](#project-setup)<br>

## Summary

SGrJavaDrivers contains libraries  that adapt the commhandler's generic device interface to device specific (communications-) transport layer.

List of current library projects:
- <b>SGrGenDriverAPI4Modbus</b>: Provides the generic driver API definitions for Modbus (as used by the SGr communication handler).
- <b>EasyModbus</b>: Consists of the Modbus driver provideded by 'Copyright (c) 2018-2020 Rossmann-Engineering' together with the SGr generic driver API adapters for ModbusRTU and ModbusTCP.

The chapters following chapters describe the architecture of the device adapters in detail.
To support their own Modbus drivers within SmartgridReady, third party providers can implement their own adapters that implement the SGrGenDriverAPI4Modbus interface.

## Generic Device Driver API

The Generic Device Driver API makes the SGr communication handler (CommHandler4Modbus) independent from the device driver implementation. The communication handler uses the same interface to communicate with any modbus driver (EasyModbus, 3rdPartyDriver).

![UML Generic Device Driver](SGrGenericDeviceDriver.png "UML Generic Device Driver")


### Component: Communicator
<table valign="top">
    <tr><td>Implementor:</td><td>Communicator provider</td></tr>
    <tr><td>Description:</td><td>The Communicator is a SGr compliant controlling device that uses the SGr Generic Interface to send commands to any device/product in the SGr environment.</tr></td>
    <tr><td valign="top">Responsibilities:</td><td>
                <p>Instantiates a concrete device driver adapter  (EasyModbusAdapter, 3rdPartyDriverAdapter...)</p>
                <p>Creates a Commhandler4Modbus instance (provided by the <b>commhandler4Modbus</b> library)</p>
                <p>Uses the CommHandler4Modbus instance to send commands to the device (readVal(), getVal())</p> </td></tr>
    <tr><td>Library:</td><td>n.a.</td></tr>                                                                                          
    <tr><td>SGrProject:</td><td><a href="https://github.com/SmartgridReady/SGrJavaSamples/tree/master/SampleCommunicator">SGrJavaSamples/SampleCommunicator<a></td></tr>                                                                                                                                                                                                                     
</table>  

<br><br>

### Component: CommHandler4Modbus
<table valign="top">
    <tr><td>Implementor:</td><td>SGr core development team</td></tr>
    <tr><td>Description:</td><td>SGr core component that maps SGr Generic Interface commands to commands of a specific product/device (External Interface), based on a device description provided in XML.
    <tr><td valign="top">Responsibilities:</td><td>
                <p>Provides the SGr Generic Interface to read and set values on the external product/device getVal(), setVal()...</p>
                <p>Converts the Generic Interface commands to device specific commands.</p>
                <p>Uses the generic interface GenDriverAPI4Modbus to send commands to the product/device</p> </td></tr>
    <tr><td>Library:</td><td><b>commmHandler4modbus.jar</b>
        <p>includes:</p>
            <ul><li>sgr-driver-api.jar</li><li>easymodbus.jar</li>
        </td></tr>                                                                                          
    <tr><td>SGrProject:</td><td><a href="https://github.com/SmartgridReady/SGrJava/tree/master/InterfaceFactory/CommHandler4Modbus">SGrJava/InterfaceFactory/CommHandler4Modbus<a></td></tr>    
</table> 

<br><br>

### Component: SGrGenDriverAPI4Modbus
<table valign="top">
    <tr><td>Implementor:</td><td>SGr core development team</td></tr>
    <tr><td>Description:</td><td><p>Defines the Java interface that must be implemented by any SGr compliant modbus device driver or driver adapter.</p>
     <p>This interface must be implemented by any modbus device SGr compliant modbus device driver.</p>
    <tr><td valign="top">Responsibilities:</td><td>
                <p>Definition of the generic device driver API used by the SGrDevice component to send commands to the products.</p>               
                </td></tr>
    <tr><td>Library:</td><td><b>sgr-driver-api.jar<b></td></tr>                                                                                          
    <tr><td>SGrProject:</td><td><a href="https://github.com/SmartgridReady/SGrJavaDrivers/tree/master/GenDriverAPI">SGrJavaDrivers/GenDriverAPI<a></td></tr>    
</table>

<br><br>

### Component: EasyModbusAdapter
<table valign="top">
    <tr><td>Implementor:</td><td>SGr core development team</td></tr>
    <tr><td>Description:</ts><td>Adapts the generic device driver API to the EasyModbus device driver.</td></tr>
    <tr><td valign="top">Responsibilities:</td><td>
                <p>Maps SGrGenDriverAPI4Modbus commands to EasyModbus commands.</p>
                <p>Maps EasyModbus specific exceptions to SGrGenDriverAPI4Modbus exceptions.</p>
                </td></tr>
    <tr><td>Library:</td><td><b>sgr-driver-api.jar<b></td></tr>                                                                                          
    <tr><td>SGrProject:</td><td><a href="https://github.com/SmartgridReady/SGrJavaDrivers/tree/master/EasyModbus">SGrJavaDrivers/EasyModbus<a></td></tr>    
</table> 

<br><br>

### Component: EasyModbus
<table valign="top">
    <tr><td>Implementor:</td><td>SGr core development team</td></tr>
    <tr><td>Description:</ts><td>Modbus device driver provided by the SGr core development team.</td></tr>
    <tr><td valign="top">Responsibilities:</td><td>
                <p>Implements a modbus device driver that supports modbus RTU and modbus overTCP</p>
                </td></tr>
    <tr><td>Library:</td><td><b>easymodbus.jar<b></td></tr>                                                                                          
    <tr><td>SGrProject:</td><td><a href="https://github.com/SmartgridReady/SGrJavaDrivers/tree/master/EasyModbus">SGrJavaDrivers/EasyModbus<a></td></tr>    
</table>

<br><br>

### Component: 3rdPartyAdapter
<table valign="top">
    <tr><td>Implementor:</td><td>3rd party provider</td></tr>
    <tr><td>Description:</ts><td>Adapts the generic device driver API to the 3rd party device driver.</td></tr>
    <tr><td valign="top">Responsibilities:</td><td>
       <p>Maps SGrGenDriverAPI4Modbus commands to 3rd party driver commands</p>
        <p>Maps 3rd party driver specific exceptions to 3rd party driver exceptions.</p>
        </td></tr>
</table>

### Component: 3rdPartyDriver
<table valign="top">
    <tr><td>Implementor:</td><td>3rd party provider</td></tr>
    <tr><td>Description:</ts><td>Proprietary Modbus driver provided by a 3rd party.</td></tr>
    <tr><td valign="top">Responsibilities:</td><td>        
       <p>Implements a 3rd party modbus driver</p>
       <p>Receive commands from the 3rdPartyAdapter.</p>
    </td></tr>
</table>


## Project Setup
