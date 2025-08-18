package com.example.autotest.backend.rest.jira.dto;

import java.util.List;
import java.util.Objects;

public class JiraIssueSearchParametersDto {

    private List<String> fields;
    private boolean fieldsByKeys;
    private String expand;
    private List<String> properties;
    private boolean updateHistory;

    public List<String> getFields() {
        return fields;
    }

    public JiraIssueSearchParametersDto setFields(List<String> fields) {
        this.fields = fields;
        return this;
    }

    public boolean isFieldsByKeys() {
        return fieldsByKeys;
    }

    public JiraIssueSearchParametersDto setFieldsByKeys(boolean fieldsByKeys) {
        this.fieldsByKeys = fieldsByKeys;
        return this;
    }

    public String getExpand() {
        return expand;
    }

    public JiraIssueSearchParametersDto setExpand(String expand) {
        this.expand = expand;
        return this;
    }

    public List<String> getProperties() {
        return properties;
    }

    public JiraIssueSearchParametersDto setProperties(List<String> properties) {
        this.properties = properties;
        return this;
    }

    public boolean isUpdateHistory() {
        return updateHistory;
    }

    public JiraIssueSearchParametersDto setUpdateHistory(boolean updateHistory) {
        this.updateHistory = updateHistory;
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
        JiraIssueSearchParametersDto that = (JiraIssueSearchParametersDto) o;

        return fieldsByKeys == that.fieldsByKeys
                && updateHistory == that.updateHistory
                && Objects.equals(fields, that.fields)
                && Objects.equals(expand, that.expand)
                && Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fields, fieldsByKeys, expand, properties, updateHistory);
    }

    @Override
    public String toString() {
        return String.format(
                "JiraIssueSearchParametersDto {%n fields=%s,%n fieldsByKeys=%s,%n expand=%s,%n properties=%s,%n updateHistory=%s%n}",
                fields,
                fieldsByKeys,
                expand,
                properties,
                updateHistory
        );
    }
}
