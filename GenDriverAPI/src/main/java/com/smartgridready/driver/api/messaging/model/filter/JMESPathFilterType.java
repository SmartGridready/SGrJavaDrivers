package com.smartgridready.driver.api.messaging.model.filter;

@SuppressWarnings("unused")
public class JMESPathFilterType {

    private final String query;
    private final String matchesRegex;

    public JMESPathFilterType(String query, String matchesRegex) {
        this.query = query;
        this.matchesRegex = matchesRegex;
    }

    public String getQuery() {
        return query;
    }

    public String getMatchesRegex() {
        return matchesRegex;
    }
}