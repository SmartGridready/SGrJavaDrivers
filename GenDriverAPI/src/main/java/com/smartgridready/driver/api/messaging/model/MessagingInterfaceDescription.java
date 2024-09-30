package com.smartgridready.driver.api.messaging.model;

import com.smartgridready.driver.api.messaging.model.authentication.MessageBrokerAuthentication;

import java.util.List;

@SuppressWarnings("unused")
public class MessagingInterfaceDescription {

    private final MessagingPlatformType messagingPlatformType;

    private final List<MessageBroker> messageBrokerList;

    private final String clientId;

    private final MessageBrokerAuthentication messageBrokerAuthentication;

    public MessagingInterfaceDescription(
            MessagingPlatformType messagingPlatformType,
            List<MessageBroker> messageBrokerList,
            MessageBrokerAuthentication messageBrokerAuthentication,
            String clientId
    ) {
        this.messagingPlatformType = messagingPlatformType;
        this.messageBrokerList = messageBrokerList;
        this.messageBrokerAuthentication = messageBrokerAuthentication;
        this.clientId = clientId;
    }

    public MessagingPlatformType getMessagingPlatformType() {
        return messagingPlatformType;
    }

    public List<MessageBroker> getMessageBrokerList() {
        return messageBrokerList;
    }

    public MessageBrokerAuthentication getMessageBrokerAuthentication() {
        return messageBrokerAuthentication;
    }

    public String getClientId() {
        return clientId;
    }
}
