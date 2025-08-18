package com.example.autotest.backend.rest.jira.aio.dto;

import com.example.autotest.backend.rest.jira.aio.type.AioConfigurableField;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class AioFieldDto implements Serializable {

    private Integer id;
    private Integer projectId;
    private AioConfigurableField configurableField;
    private String updatedById;
    private Long updatedDate;

    @JsonProperty("ID")
    public Integer getId() {
        return id;
    }

    @JsonProperty("ID")
    public AioFieldDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public AioFieldDto setProjectId(Integer projectId) {
        this.projectId = projectId;
        return this;
    }

    public AioConfigurableField getConfigurableField() {
        return configurableField;
    }

    public AioFieldDto setConfigurableField(AioConfigurableField configurableField) {
        this.configurableField = configurableField;
        return this;
    }

    @JsonProperty("updatedByID")
    public String getUpdatedById() {
        return updatedById;
    }

    @JsonProperty("updatedByID")
    public AioFieldDto setUpdatedById(String updatedById) {
        this.updatedById = updatedById;
        return this;
    }

    public Long getUpdatedDate() {
        return updatedDate;
    }

    public AioFieldDto setUpdatedDate(Long updatedDate) {
        this.updatedDate = updatedDate;
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
        AioFieldDto that = (AioFieldDto) o;

        return Objects.equals(id, that.id)
                && Objects.equals(projectId, that.projectId)
                && configurableField == that.configurableField
                && Objects.equals(updatedById, that.updatedById)
                && Objects.equals(updatedDate, that.updatedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, projectId, configurableField, updatedById, updatedDate);
    }

    @Override
    public String toString() {
        return String.format(
                "AioFieldDto {%n id=%s,%n projectId=%s,%n configurableField=%s,%n updatedById=%s,%n updatedDate=%s%n}",
                id,
                projectId,
                configurableField,
                updatedById,
                updatedDate
        );
    }
}
