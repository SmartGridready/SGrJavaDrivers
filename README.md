# SGrJavaDrivers

## Index

[Summary](#summary)  
[Project setup for developers](#project-setup)  
[Build and publish for development](#build-and-publish-for-development)  
[Build and publish a release](#build-and-publish-a-release)

## Summary

SGrJavaDrivers contains libraries that adapt the commhandler's generic device interface to device-specific transport layer.

List of current library projects:

- **j2mod**: Contains a Modbus driver backed by [j2mod](https://github.com/steveohara/j2mod). This is the recommended Modbus driver.
- **EasyModbus**: Consists of the Modbus driver provided by `Copyright (c) 2018-2020 Rossmann-Engineering` together with the SGr generic driver API adapters for ModbusRTU and ModbusTCP. Note that this implementation is no longer recommended.
- **ApacheHttp**: Contains an HTTP/REST API driver based on the _Apache_ client libraries.
- **HiveMq**: Contains a messaging driver which supports MQTT, using the _HiveMQ_ client library.

The following chapters describe the architecture of the device adapters in detail.
To support their own communication interface drivers within SmartGridready, third party providers can implement their own adapters that implement the generic driver API interface.

## Generic Device Driver API

The generic device driver API makes the SGr communication handler (`GenDeviceApi`) independent from the device driver implementation.
The communication handler uses the same interfaces to communicate with any communication interface driver (`j2mod`, `EasyModbus`, 3rd-party drivers etc.).

The source code can be found in the [SGrJava repository](https://github.com/SmartGridready/SGrJava).

Examples of communicator implementations can be found in the [SGrJavaSamples repository](https://github.com/SmartGridready/SGrJavaSamples).

## Develop your own Driver Implementation

### General

It is recommended to copy the project structure (Gradle build files, sources) from existing driver implementations.

Depending on the type of communication interface, you need to implement different classes.
In general, you need to implement a factory class which creates new instances and the actual driver implementation.

The communication handler can automatically detect driver implementations when they are present in the classpath,
by using the _ServiceLoader_ mechanism. You must specify the appropriate factory interface class under `src/main/resources/META-INF/services`.
In case of Modbus: a file `com.smartgridready.driver.api.modbus.GenDriverAPI4ModbusFactory` containing the factory implementation, e.g. `com.smartgridready.driver.j2mod.J2ModModbusClientFactory`.

When using the _ServiceLoader_ mechanism, only add a single driver implementation per interface type to the classpath.
Otherwise, the communication handler may get confused or simply use the first implementation it detects.

### Modbus

You must implement the following interfaces:

- `GenDriverAPI4Modbus`
- `GenDriverAPI4ModbusFactory`

### REST / HTTP

You must implement the following interfaces:

- `GenHttpRequest`
- `GenUriBuilder`
- `GenHttpClientFactory`

### Messaging

You must implement the following interfaces:

- `GenMessagingClient` (once per platform)
- `GenMessagingClientFactory`

The messaging driver API supports multiple platforms, such as _MQTT_ or _Apache Kafka_. It is up to the developer which platforms are to be implemented.

### Contacts

You must implement the following interfaces:

- `GenDriverAPI4Contacts`
- `GenDriverAPI4ContactsFactory`

Due to limitations in the current SGr specification, it is difficult to find out how many I/Os are present and how to address them.
This makes implementation of a generic interface for contacts (digital I/Os) that supports different hardware.
At this moment, it is not recommended to implement a custom contacts driver.

## Build and Publish for Development

### Prerequisites

1. Java JDK version >= 11

2. **To build generic driver API if required:** Clone the SGrJava repository <https://github.com/SmartGridready/SGrJava/> in parallel to the SGrJavaDrivers repository.

    ```text
    Required folder structure:

    sgr-projects-root
    |_SGrJavaDrivers
    |_SGrJava
    ```

3. Go to `SGrJava/GenDriverAPI` and execute `./gradlew publishToMavenLocal`.

4. Set `snapshots=sgr-driver-api` in your driver project's `gradle.properties`.

### Run the gradle build and publish to the local maven repository

Each project can be built separately:

- To build the project run in the project root of the respective project (CommHandler, SGrSpecification, GenDriverAPI):

    ```bash
    ./gradlew clean build
    ```

- To publish to the local Maven repository run:

    ```bash
    ./gradlew publishToMavenLocal
    ```

The driver implementation only depends on the `sgr-driver-api` package.
