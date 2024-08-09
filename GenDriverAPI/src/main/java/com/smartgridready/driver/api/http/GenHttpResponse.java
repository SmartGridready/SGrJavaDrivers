package com.smartgridready.driver.api.http;

public class GenHttpResponse {

    private final static int SC_OK = 200;
    private static final int SC_CLIENT_ERROR = 400;

    final private String response;
    final private int responseCode;
    final private String reason;

    private GenHttpResponse(String response, int responseCode, String reason) {
        this.response = response;
        this.responseCode = responseCode;
        this.reason = reason;
    }

    public String getResponse() {
        return response;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getReason() {
        return reason;
    }

    public boolean isOk() {
        return (responseCode >= SC_OK) && (responseCode < SC_CLIENT_ERROR);
    }

    public static GenHttpResponse of(String response) {
        return new GenHttpResponse(response, SC_OK, "");
    }

    public static GenHttpResponse of(String response, int responseCode, String reason) {
        return new GenHttpResponse(response, responseCode, reason);
    }
}
