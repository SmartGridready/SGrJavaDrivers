package com.smartgridready.driver.api.messaging.model.filter;

@SuppressWarnings("unused")
public class RegexFilterType {

    private final java.lang.String query;
    private final java.lang.String matchesRegex;

    public RegexFilterType(String query, String matchesRegex) {
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
