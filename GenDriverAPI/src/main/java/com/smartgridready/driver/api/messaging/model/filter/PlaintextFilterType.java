package com.smartgridready.driver.api.messaging.model.filter;

@SuppressWarnings("unused")
public class PlaintextFilterType {

    private final String matchesRegex;

    public PlaintextFilterType(String matchesRegex) {
        this.matchesRegex = matchesRegex;
    }

    public String getMatchesRegex() {
        return matchesRegex;
    }
}
