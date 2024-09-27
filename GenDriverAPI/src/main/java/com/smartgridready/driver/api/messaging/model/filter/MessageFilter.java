package com.smartgridready.driver.api.messaging.model.filter;

@SuppressWarnings("unused")
public class MessageFilter {
    private final PlaintextFilterType plaintextFilter;
    private final JMESPathFilterType jmespathFilter;
    private final XPathFilterType xpapathFilter;
    private final RegexFilterType regexFilter;

    public MessageFilter(PlaintextFilterType plaintextFilter, JMESPathFilterType jmespathFilter, XPathFilterType xpapathFilter, RegexFilterType regexFilter) {
        this.plaintextFilter = plaintextFilter;
        this.jmespathFilter = jmespathFilter;
        this.xpapathFilter = xpapathFilter;
        this.regexFilter = regexFilter;
    }

    public PlaintextFilterType getPlaintextFilter() {
        return plaintextFilter;
    }

    public JMESPathFilterType getJmespathFilter() {
        return jmespathFilter;
    }

    public XPathFilterType getXpapathFilter() {
        return xpapathFilter;
    }

    public RegexFilterType getRegexFilter() {
        return regexFilter;
    }
}
