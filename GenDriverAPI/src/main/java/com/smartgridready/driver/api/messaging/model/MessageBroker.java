package com.smartgridready.driver.api.messaging.model;

@SuppressWarnings("unused")
public class MessageBroker {

    private final String host;

    private final String port;

    private final boolean tls;

    private final boolean tlsVerifyCertificate;

    public MessageBroker(String host, String port, boolean tls, boolean tlsVerifyCertificate) {
        this.host = host;
        this.port = port;
        this.tls = tls;
        this.tlsVerifyCertificate = tlsVerifyCertificate;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public boolean isTls() {
        return tls;
    }

    public boolean isTlsVerifyCertificate() {
        return tlsVerifyCertificate;
    }
}
