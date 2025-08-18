package com.example.autotest.backend.rest.jira.aio.dto;

import com.example.autotest.backend.rest.jira.aio.type.AioTestScriptType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class AioTestCaseScriptTypeDto implements Serializable {

    private Integer id;
    private String name;
    private String description;
    private AioTestScriptType testScriptType;
    private Long createdDate;
    private Long updatedDate;
    private Boolean isEnabled;

    @JsonProperty("ID")
    public Integer getId() {
        return id;
    }

    @JsonProperty("ID")
    public AioTestCaseScriptTypeDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public AioTestCaseScriptTypeDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AioTestCaseScriptTypeDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public AioTestScriptType getTestScriptType() {
        return testScriptType;
    }

    public AioTestCaseScriptTypeDto setTestScriptType(AioTestScriptType testScriptType) {
        this.testScriptType = testScriptType;
        return this;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public AioTestCaseScriptTypeDto setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public Long getUpdatedDate() {
        return updatedDate;
    }

    public AioTestCaseScriptTypeDto setUpdatedDate(Long updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public Boolean getEnabled() {
        return isEnabled;
    }

    public AioTestCaseScriptTypeDto setEnabled(Boolean enabled) {
        isEnabled = enabled;
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
        AioTestCaseScriptTypeDto that = (AioTestCaseScriptTypeDto) o;

        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && testScriptType == that.testScriptType
                && Objects.equals(createdDate, that.createdDate)
                && Objects.equals(updatedDate, that.updatedDate)
                && Objects.equals(isEnabled, that.isEnabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, testScriptType, createdDate, updatedDate, isEnabled);
    }

    @Override
    public String toString() {
        return String.format(
                "AioTestCaseScriptTypeDto {%n id=%s,%n name=%s,%n description=%s,%n testScriptType=%s,%n createdDate=%s,%n updatedDate=%s,%n isEnabled=%s%n}",
                id,
                name,
                description,
                testScriptType,
                createdDate,
                updatedDate,
                isEnabled
        );
    }
}
