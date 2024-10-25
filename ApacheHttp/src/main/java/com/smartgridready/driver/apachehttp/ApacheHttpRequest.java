package com.smartgridready.driver.apachehttp;

import com.smartgridready.driver.api.http.GenHttpResponse;
import com.smartgridready.driver.api.http.GenHttpRequest;
import com.smartgridready.driver.api.http.HttpMethod;
import org.apache.commons.io.IOUtils;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ApacheHttpRequest implements GenHttpRequest {

    private HttpMethod httpMethod;

    private URI uri;

    private final Map<String, String> formParams = new HashMap<>();

    private String body;

    private final Map<String, String> headers = new HashMap<>();

    private static final EnumMap<HttpMethod, Function<String, Request>> HTTP_METHOD_MAP = new EnumMap<>(HttpMethod.class);

    private static final Map<String, Function31<Request, String, ContentType, Request>> BODY_ENCODE_MAP = new HashMap<>();

    static {
        HTTP_METHOD_MAP.put(HttpMethod.GET, Request::get);
        HTTP_METHOD_MAP.put(HttpMethod.POST, Request::post);
        HTTP_METHOD_MAP.put(HttpMethod.PUT, Request::put);
        HTTP_METHOD_MAP.put(HttpMethod.PATCH, Request::patch);
        HTTP_METHOD_MAP.put(HttpMethod.DELETE, Request::delete);

        BODY_ENCODE_MAP.put(ContentType.TEXT_PLAIN.getMimeType(), ApacheHttpRequest::encodeStringBody);
        BODY_ENCODE_MAP.put(ContentType.APPLICATION_JSON.getMimeType(), ApacheHttpRequest::encodeStringBody);
    }

    @Override
    public GenHttpResponse execute() throws IOException {

        Function<String, Request> requestFactoryFunct = HTTP_METHOD_MAP.get(httpMethod);
        if (requestFactoryFunct == null) {
            throw new IOException(String.format("invalid HTTP method '%s'", httpMethod.name()));
        }

        Request httpReq = requestFactoryFunct.apply(uri.toString());
        ContentType requestContentType = prepareHttpHeaders(httpReq);

        if (!formParams.isEmpty()) {
            final List<NameValuePair> nameValuePairList = formParams
                .entrySet()
                .stream()
                .map(e -> new BasicNameValuePair(e.getKey(), e.getValue()))
                .collect(Collectors.toList());
            httpReq.body(new UrlEncodedFormEntity(nameValuePairList, StandardCharsets.UTF_8));
        } else if (body != null) {
            Function31<Request, String, ContentType, Request> bodyEncodeFunct = BODY_ENCODE_MAP.get(requestContentType.getMimeType());
            if (bodyEncodeFunct == null) {
                throw new IOException(String.format("Cannot encode request body of content type '%s'", requestContentType.getMimeType()));
            }
            bodyEncodeFunct.apply(httpReq, body, requestContentType);
        }

        HttpResponse httpResp = httpReq.execute().returnResponse();
        if ((httpResp.getCode() >= HttpStatus.SC_OK) && (httpResp.getCode() < HttpStatus.SC_CLIENT_ERROR)) {
            try (InputStream is = ((ClassicHttpResponse)httpResp).getEntity().getContent()) {

                String contentTypeStr = ((ClassicHttpResponse)httpResp).getEntity().getContentType();
                ContentType contentType = (contentTypeStr != null) ? ContentType.parse(contentTypeStr) : ContentType.DEFAULT_TEXT;

                String content = IOUtils.toString(is, contentType.getCharset());
                return GenHttpResponse.of(content, httpResp.getCode(), httpResp.getReasonPhrase());
            }
        }
        return GenHttpResponse.of("", httpResp.getCode(), httpResp.getReasonPhrase());
    }

    @Override
    public GenHttpRequest setUri(URI uri) {
        this.uri = uri;
        return this;
    }

    @Override
    public void setHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    @Override
    public void addHeader(String key, String value) {
        this.headers.put(key, value);
    }

    @Override
    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public void addFormParam(String key, String value ) {
        this.formParams.put(key, value);
    }

    private ContentType prepareHttpHeaders(Request httpReq) {
        ContentType requestContentType = ContentType.TEXT_PLAIN;

        if (!headers.isEmpty()) {
            for (var headerEntry : headers.entrySet()) {
                if (headerEntry.getKey().equalsIgnoreCase(HttpHeaders.CONTENT_TYPE)) {
                    // content type header will be set later in body encode function
                    requestContentType = ContentType.parse(headerEntry.getValue());
                } else {
                    httpReq.addHeader(headerEntry.getKey(), headerEntry.getValue());
                }
            }
        }
        return requestContentType;
    }

    static Request encodeStringBody(Request httpReq, String content, ContentType contentType) {
        return httpReq.body(new StringEntity(content, contentType));
    }

    @SuppressWarnings("UnusedReturnValue")
    @FunctionalInterface
    private interface Function31<A, B, C, R> {
        R apply(A a, B b, C c);
    }

}
