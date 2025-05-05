package com.smartgridready.driver.apachehttp.security;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * A dummy implementation of a host name verifier.
 * Ignores validity of host names.
 */
public class NonValidatingHostnameVerifier implements HostnameVerifier {

    private static HostnameVerifier instance = null; 

    /**
     * Gets the singleton instance.
     * @return an instance of {@code NonValidatingHostnameVerifier}
     */
    public static synchronized HostnameVerifier getInstance() {
        if (instance == null) instance = new NonValidatingHostnameVerifier();
        return instance;
    }

    private NonValidatingHostnameVerifier() {}

    /**
     * Verifies a host name.
     * Always returns true.
     * @param hostname the host name
     * @param session the SSL session
     * @return a boolean
     */
    @Override
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
}
