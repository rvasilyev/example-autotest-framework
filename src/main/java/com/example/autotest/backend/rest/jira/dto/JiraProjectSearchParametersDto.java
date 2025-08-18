package com.example.autotest.backend.rest.jira.dto;

import java.util.List;
import java.util.Objects;

public class JiraProjectSearchParametersDto {

    private String expand;
    private List<String> properties;

    public String getExpand() {
        return expand;
    }

    public JiraProjectSearchParametersDto setExpand(String expand) {
        this.expand = expand;
        return this;
    }

    public List<String> getProperties() {
        return properties;
    }

    public JiraProjectSearchParametersDto setProperties(List<String> properties) {
        this.properties = properties;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JiraProjectSearchParametersDto that = (JiraProjectSearchParametersDto) o;

        return Objects.equals(expand, that.expand) && Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expand, properties);
    }

    @Override
    public String toString() {
        return String.format("JiraProjectSearchParametersDto {%n expand=%s,%n properties=%s%n}", expand, properties);
    }
}
