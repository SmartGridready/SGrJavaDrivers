package com.smartgridready.driver.api.messaging.model.authentication;

@SuppressWarnings("unused")
public class MessageBrokerAuthenticationBasic {

    private final String username;
    private final String password;

    public MessageBrokerAuthenticationBasic(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
