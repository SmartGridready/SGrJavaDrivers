package com.smartgridready.driver.api.http;

import java.io.IOException;
import java.net.URI;

public interface GenHttpRequest {

    GenHttpResponse execute() throws IOException;

    GenHttpRequest setUri(URI uri);

    void setHttpMethod(HttpMethod httpMethod);

    void addHeader(String key, String value);

    void setBody(String body);

    void addFormParam(String key, String value);
}
