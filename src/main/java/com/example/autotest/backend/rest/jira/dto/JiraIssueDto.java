package com.example.autotest.backend.rest.jira.dto;

import java.io.Serializable;
import java.util.Objects;

public class JiraIssueDto implements Serializable {

    private String expand;
    private String id;
    private String self;
    private String key;
    private transient Object fields;

    public String getExpand() {
        return expand;
    }

    public JiraIssueDto setExpand(String expand) {
        this.expand = expand;
        return this;
    }

    public String getId() {
        return id;
    }

    public JiraIssueDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getSelf() {
        return self;
    }

    public JiraIssueDto setSelf(String self) {
        this.self = self;
        return this;
    }

    public String getKey() {
        return key;
    }

    public JiraIssueDto setKey(String key) {
        this.key = key;
        return this;
    }

    public Object getFields() {
        return fields;
    }

    public JiraIssueDto setFields(Object fields) {
        this.fields = fields;
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
        JiraIssueDto that = (JiraIssueDto) o;

        return Objects.equals(expand, that.expand)
                && Objects.equals(id, that.id)
                && Objects.equals(self, that.self)
                && Objects.equals(key, that.key)
                && Objects.equals(fields, that.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expand, id, self, key, fields);
    }

    @Override
    public String toString() {
        return String.format(
                "JiraIssueDto {%n expand=%s,%n id=%s,%n self=%s,%n key=%s,%n fields=%s%n}",
                expand,
                id,
                self,
                key,
                fields
        );
    }
}
