package com.smartgridready.driver.api.messaging.model.authentication;

@SuppressWarnings("unused")
public class MessageBrokerAuthentication {

    private final MessageBrokerAuthenticationBasic basicAuthentication;

    private final MessageBrokerAuthenticationClientCertificate clientCertificateAuthentication;

    public MessageBrokerAuthentication(MessageBrokerAuthenticationBasic basicAuthentication, MessageBrokerAuthenticationClientCertificate clientCertificateAuthentication) {
        this.basicAuthentication = basicAuthentication;
        this.clientCertificateAuthentication = clientCertificateAuthentication;
    }

    public MessageBrokerAuthenticationBasic getBasicAuthentication() {
        return basicAuthentication;
    }

    public MessageBrokerAuthenticationClientCertificate getClientCertificateAuthentication() {
        return clientCertificateAuthentication;
    }
}
