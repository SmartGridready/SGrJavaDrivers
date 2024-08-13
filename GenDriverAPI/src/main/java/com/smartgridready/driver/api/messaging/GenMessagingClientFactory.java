package com.smartgridready.driver.api.messaging;

import com.smartgridready.ns.v0.MessagingInterfaceDescription;

/**
 * Interface to be used to create smartgridready messaging clients.
 */
public interface GenMessagingClientFactory {

    /**
     * Factory method to create a new instance of the smartgridready messaging client.
     *
     * @param interfaceDescription Describes the messaging interface and it's parameters.
     * @return A new messaging client instance.
     */
    GenMessagingClient create(MessagingInterfaceDescription interfaceDescription);
}
