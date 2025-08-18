package com.example.autotest.backend.rest.jira.aio.dto;

import com.example.autotest.backend.rest.jira.aio.type.AioCustomFieldType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class AioCustomFieldTypeDto implements Serializable {

    private Integer id;
    private AioCustomFieldType type;
    private String name;
    private String description;

    @JsonProperty("ID")
    public Integer getId() {
        return id;
    }

    @JsonProperty("ID")
    public AioCustomFieldTypeDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public AioCustomFieldType getType() {
        return type;
    }

    public AioCustomFieldTypeDto setType(AioCustomFieldType type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public AioCustomFieldTypeDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AioCustomFieldTypeDto setDescription(String description) {
        this.description = description;
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
        AioCustomFieldTypeDto that = (AioCustomFieldTypeDto) o;

        return Objects.equals(id, that.id)
                && type == that.type && Objects.equals(name, that.name)
                && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, type, name, description);
    }

    @Override
    public String toString() {
        return String.format(
                "AioCustomFieldTypeDto {%n id=%s,%n type=%s,%n name=%s,%n description=%s%n}",
                id,
                type,
                name,
                description
        );
    }
}
