package com.smartgridready.driver.apachehttp;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hc.core5.net.URIBuilder;

import com.smartgridready.driver.api.http.GenUriBuilder;

public class ApacheUriBuilder implements GenUriBuilder {

    private URIBuilder builder;

    public ApacheUriBuilder(String baseUri) throws URISyntaxException {
        this.builder = new URIBuilder(baseUri);
    }

    @Override
    public GenUriBuilder addPath(String path) {
        builder.appendPath(path);
        return this;
    }

    @Override
    public GenUriBuilder addQueryParameter(String name, String value) {
        builder.addParameter(name, value);
        return this;
    }

    @Override
    public GenUriBuilder setQueryString(String query) {
        builder.clearParameters();
        builder.setCustomQuery(query);
        return this;
    }

    @Override
    public URI build() throws URISyntaxException {
        return builder.build();
    }
}
