package com.smartgridready.driver.hivemq.security;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * A dummy implementation of a host name verifier.
 */
public class NonValidatingHostnameVerifier implements HostnameVerifier {

    private static HostnameVerifier instance = null; 

    public static synchronized HostnameVerifier getInstance() {
        if (instance == null) instance = new NonValidatingHostnameVerifier();
        return instance;
    }

    private NonValidatingHostnameVerifier() {}

    @Override
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
}
