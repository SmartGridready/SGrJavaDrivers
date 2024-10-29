package com.smartgridready.driver.hivemq;

import java.util.Collections;
import java.util.Set;

import com.smartgridready.driver.api.messaging.GenMessagingClient;
import com.smartgridready.driver.api.messaging.GenMessagingClientFactory;
import com.smartgridready.driver.api.messaging.model.MessagingInterfaceDescription;
import com.smartgridready.driver.api.messaging.model.MessagingPlatformType;

public class HiveMqtt5MessagingClientFactory implements GenMessagingClientFactory {

    @Override
    public GenMessagingClient create(MessagingInterfaceDescription interfaceDescription) {

        if (interfaceDescription.getMessagingPlatformType() == null) {
            throw new IllegalArgumentException("No messaging platform defined in EI-XML");
        }

        if (interfaceDescription.getMessageBrokerList() == null ) {
            throw new IllegalArgumentException("No message broker defined in EI-XML");
        }

        if (interfaceDescription.getMessagingPlatformType() != MessagingPlatformType.MQTT5) {
            throw new IllegalArgumentException(
                    "Wrong client factory provided (supporting MQTT5). " +
                            "Please use a messaging client factory that supports " +
                            interfaceDescription.getMessagingPlatformType().name());
        }
        return new HiveMqtt5MessagingClient(interfaceDescription);
    }

    @Override
    public Set<MessagingPlatformType> getSupportedPlatforms() {
        return Collections.singleton(MessagingPlatformType.MQTT5);
    }
}
