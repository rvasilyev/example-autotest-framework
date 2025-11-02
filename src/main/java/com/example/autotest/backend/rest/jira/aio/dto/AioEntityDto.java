package com.example.autotest.backend.rest.jira.aio.dto;

import com.example.autotest.backend.rest.jira.aio.type.AioEntityType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class AioEntityDto {

    private Integer id;
    private String name;
    private String namePlural;
    private String description;
    private AioEntityType entityType;
    private Boolean canBeRenamed;
    private AioEntityType tcmsEntityType;

    @JsonProperty("ID")
    public Integer getId() {
        return id;
    }

    @JsonProperty("ID")
    public AioEntityDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public AioEntityDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getNamePlural() {
        return namePlural;
    }

    public AioEntityDto setNamePlural(String namePlural) {
        this.namePlural = namePlural;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AioEntityDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public AioEntityType getEntityType() {
        return entityType;
    }

    public AioEntityDto setEntityType(AioEntityType entityType) {
        this.entityType = entityType;
        return this;
    }

    public Boolean getCanBeRenamed() {
        return canBeRenamed;
    }

    public AioEntityDto setCanBeRenamed(Boolean canBeRenamed) {
        this.canBeRenamed = canBeRenamed;
        return this;
    }

    public AioEntityType getTcmsEntityType() {
        return tcmsEntityType;
    }

    public AioEntityDto setTcmsEntityType(AioEntityType tcmsEntityType) {
        this.tcmsEntityType = tcmsEntityType;
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
        AioEntityDto that = (AioEntityDto) o;

        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(namePlural, that.namePlural)
                && Objects.equals(description, that.description)
                && entityType == that.entityType
                && Objects.equals(canBeRenamed, that.canBeRenamed)
                && tcmsEntityType == that.tcmsEntityType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, namePlural, description, entityType, canBeRenamed, tcmsEntityType);
    }

    @Override
    public String toString() {
        return String.format(
                "AioEntityDto {%n id=%s,%n name=%s,%n namePlural=%s,%n description=%s,%n entityType=%s,%n canBeRenamed=%s,%n tcmsEntityType=%s%n}",
                id,
                name,
                namePlural,
                description,
                entityType,
                canBeRenamed,
                tcmsEntityType
        );
    }
}
