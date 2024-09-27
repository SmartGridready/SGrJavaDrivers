package com.smartgridready.driver.api.messaging.model.authentication;

@SuppressWarnings("unused")
public class MessageBrokerAuthenticationClientCertificate {

    private final String keystorePath;

    private final String keystorePassword;

    private final String truststorePath;

    private final String truststorePassword;

    public MessageBrokerAuthenticationClientCertificate(String keystorePath, String keystorePassword, String truststorePath, String truststorePassword) {
        this.keystorePath = keystorePath;
        this.keystorePassword = keystorePassword;
        this.truststorePath = truststorePath;
        this.truststorePassword = truststorePassword;
    }

    public String getKeystorePath() {
        return keystorePath;
    }

    public String getKeystorePassword() {
        return keystorePassword;
    }

    public String getTruststorePath() {
        return truststorePath;
    }

    public String getTruststorePassword() {
        return truststorePassword;
    }
}
