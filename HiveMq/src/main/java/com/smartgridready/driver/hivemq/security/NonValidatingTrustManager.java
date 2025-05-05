package com.smartgridready.driver.hivemq.security;

import javax.net.ssl.X509TrustManager;

/**
 * A trust manager which ignores certificate validity.
 */
public class NonValidatingTrustManager implements X509TrustManager {

    /**
     * Construct.
     */
    public NonValidatingTrustManager() {}

    /**
     * Gets the accepted certificate issues.
     * Always returns null.
     * @return an array of {@code java.security.cert.X509Certificate}
     */
    @Override
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    /**
     * Checks if a client is trusted.
     * @param chain the certificate chain
     * @param authType the type of authentication
     */
    @Override
    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        // nothing
    }

    /**
     * Checks if a server is trusted.
     * @param chain the certificate chain
     * @param authType the type of authentication
     */
    @Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        // nothing
    }
}