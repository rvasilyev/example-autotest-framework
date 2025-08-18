package com.example.autotest.backend.rest.jira.aio.dto;

import com.example.autotest.backend.rest.jira.aio.type.AioCustomFieldJiraType;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AioCustomFieldJiraTypeDto implements Serializable {

    private AioCustomFieldJiraType type;
    private transient Object jiraFixedTypes;
    private List<String> jiraCustomSchemaTypes = Collections.emptyList();
    private List<String> jiraUserCustomTypes = Collections.emptyList();

    public AioCustomFieldJiraType getType() {
        return type;
    }

    public AioCustomFieldJiraTypeDto setType(AioCustomFieldJiraType type) {
        this.type = type;
        return this;
    }

    public Object getJiraFixedTypes() {
        return jiraFixedTypes;
    }

    public AioCustomFieldJiraTypeDto setJiraFixedTypes(Object jiraFixedTypes) {
        this.jiraFixedTypes = jiraFixedTypes;
        return this;
    }

    public List<String> getJiraCustomSchemaTypes() {
        return jiraCustomSchemaTypes;
    }

    public AioCustomFieldJiraTypeDto setJiraCustomSchemaTypes(List<String> jiraCustomSchemaTypes) {
        this.jiraCustomSchemaTypes = jiraCustomSchemaTypes;
        return this;
    }

    public List<String> getJiraUserCustomTypes() {
        return jiraUserCustomTypes;
    }

    public AioCustomFieldJiraTypeDto setJiraUserCustomTypes(List<String> jiraUserCustomTypes) {
        this.jiraUserCustomTypes = jiraUserCustomTypes;
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
        AioCustomFieldJiraTypeDto that = (AioCustomFieldJiraTypeDto) o;

        return type == that.type
                && Objects.equals(jiraFixedTypes, that.jiraFixedTypes)
                && Objects.equals(jiraCustomSchemaTypes, that.jiraCustomSchemaTypes)
                && Objects.equals(jiraUserCustomTypes, that.jiraUserCustomTypes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, jiraFixedTypes, jiraCustomSchemaTypes, jiraUserCustomTypes);
    }

    @Override
    public String toString() {
        return String.format(
                "AioCustomFieldJiraTypeDto {%n type=%s,%n jiraFixedTypes=%s,%n jiraCustomSchemaTypes=%s,%n jiraUserCustomTypes=%s%n}",
                type,
                jiraFixedTypes,
                jiraCustomSchemaTypes,
                jiraUserCustomTypes
        );
    }
}
