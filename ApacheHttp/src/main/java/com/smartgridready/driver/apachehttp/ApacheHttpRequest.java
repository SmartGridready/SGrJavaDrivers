package com.smartgridready.driver.apachehttp;

import com.smartgridready.driver.api.http.GenHttpResponse;
import com.smartgridready.driver.apachehttp.security.NonValidatingHostnameVerifier;
import com.smartgridready.driver.api.http.GenHttpRequest;
import com.smartgridready.driver.api.http.HttpMethod;
import org.apache.commons.io.IOUtils;
import org.apache.hc.client5.http.entity.UrlEncodedFormEntity;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.SSLInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.net.ssl.SSLContext;

public class ApacheHttpRequest implements GenHttpRequest {

    private static final Logger LOG = LoggerFactory.getLogger(ApacheHttpRequest.class);

    private HttpClientConnectionManager clientConnectionManager;

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

    public ApacheHttpRequest() {
        this(true);
    }

    public ApacheHttpRequest(boolean verifyCertificate) {
        this.clientConnectionManager = null;
        try {
			final SSLContext sslContext = verifyCertificate
				? SSLContexts.createSystemDefault()
				: SSLContexts.custom()
			        .loadTrustMaterial(TrustAllStrategy.INSTANCE)
			        .build();

			final SSLConnectionSocketFactory sslFactory = verifyCertificate
				? new SSLConnectionSocketFactory(sslContext)
				: new SSLConnectionSocketFactory(sslContext, NonValidatingHostnameVerifier.getInstance());
	
            this.clientConnectionManager = PoolingHttpClientConnectionManagerBuilder
				.create()
				.setSSLSocketFactory(sslFactory)
				.build();

		} catch (SSLInitializationException | KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
			LOG.error("SSL initialization error", e);
		}
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

        HttpResponse httpResp = httpReq.execute(buildClient()).returnResponse();
        if ((httpResp.getCode() >= HttpStatus.SC_OK) && (httpResp.getCode() < HttpStatus.SC_CLIENT_ERROR)) {
            try (InputStream is = ((ClassicHttpResponse)httpResp).getEntity().getContent()) {

                String contentTypeStr = ((ClassicHttpResponse)httpResp).getEntity().getContentType();
                ContentType contentType = (contentTypeStr != null) ? ContentType.parse(contentTypeStr) : ContentType.DEFAULT_TEXT;
                
                String content = IOUtils.toString(is, contentType.getCharset());
                return GenHttpResponse.of(content, httpResp.getCode(), httpResp.getReasonPhrase());
            }
        } else {
            var response = (ClassicHttpResponse)httpResp;
            var errorMsg = "";
            var statusCode = 0;
            try {
                statusCode = httpResp.getCode();
                errorMsg = String.format("%s: %s", httpResp.getReasonPhrase(), response.getEntity() != null ? EntityUtils.toString(response.getEntity()) : "");
            } catch (ParseException e) {
                errorMsg = String.format("%s: %s: %s", httpResp.getReasonPhrase(), "Parse error while parsing error response from http server", e.getMessage());
            } finally {
                response.close();
            }
            LOG.error("Http Server response error: {}", errorMsg);
            return GenHttpResponse.of("", statusCode, errorMsg);
        }
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

    private CloseableHttpClient buildClient() {
        HttpClientBuilder builder = HttpClients
            .custom()
            .useSystemProperties();
        
        if (clientConnectionManager != null) {
            builder = builder.setConnectionManager(clientConnectionManager);
        }
        
        return builder.build();
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
