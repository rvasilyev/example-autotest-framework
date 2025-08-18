package com.example.autotest.backend.rest.jira.aio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class AioFolderDto implements Serializable {

    private Integer id;
    private String name;
    private Integer parentId;

    @JsonProperty("ID")
    public Integer getId() {
        return id;
    }

    @JsonProperty("ID")
    public AioFolderDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public AioFolderDto setName(String name) {
        this.name = name;
        return this;
    }

    @JsonProperty("parentID")
    public Integer getParentId() {
        return parentId;
    }

    @JsonProperty("parentID")
    public AioFolderDto setParentid(Integer parentId) {
        this.parentId = parentId;
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
        AioFolderDto that = (AioFolderDto) o;

        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(parentId, that.parentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, parentId);
    }

    @Override
    public String toString() {
        return String.format("AioFolderDto {%n id=%s,%n name=%s,%n parentId=%s%n}", id, name, parentId);
    }
}
