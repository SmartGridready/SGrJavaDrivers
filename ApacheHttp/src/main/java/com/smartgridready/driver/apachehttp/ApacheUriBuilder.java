package com.smartgridready.driver.apachehttp;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hc.core5.net.URIBuilder;

import com.smartgridready.driver.api.http.GenUriBuilder;

/**
 * Implements an URI builder.
 * Uses a fluent API.
 */
public class ApacheUriBuilder implements GenUriBuilder {

    private URIBuilder builder;

    /**
     * Construct.
     * @param baseUri the base URI to start with
     * @throws URISyntaxException when the URI cannot be built
     */
    public ApacheUriBuilder(String baseUri) throws URISyntaxException {
        this.builder = new URIBuilder(baseUri);
    }

    /**
     * Adds a request path.
     * @param path the request path
     * @return the same instance of {@code ApacheUriBuilder}
     */
    @Override
    public GenUriBuilder addPath(String path) {
        builder.appendPath(path);
        return this;
    }

    /**
     * Adds a query parameter.
     * @param name the parameter name
     * @param value the parameter value
     * @return the same instance of {@code ApacheUriBuilder}
     */
    @Override
    public GenUriBuilder addQueryParameter(String name, String value) {
        builder.addParameter(name, value);
        return this;
    }

    /**
     * Sets the raw query string, overriding query parameters.
     * @param queryString the raw query string
     * @return the same instance of {@code ApacheUriBuilder}
     */
    @Override
    public GenUriBuilder setQueryString(String queryString) {
        builder.clearParameters();
        builder.setCustomQuery(queryString);
        return this;
    }

    /**
     * Builds the final URI.
     * @return a new instance of {@code URI}
     * @throws URISyntaxException when URI cannot be built
     */
    @Override
    public URI build() throws URISyntaxException {
        return builder.build();
    }
}
