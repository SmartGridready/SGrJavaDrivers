package com.smartgridready.driver.hivemq.security;

import javax.net.ssl.X509TrustManager;

/**
 * A trust manager which ignores certificate validity.
 */
public class NonValidatingTrustManager implements X509TrustManager {

    @Override
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    @Override
    public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        // nothing
    }
    @Override
    public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
        // nothing
    }
}