package com.example.autotest.backend.rest.jira.aio.dto;

import com.example.autotest.backend.rest.jira.aio.type.AioTestAutomationStatusType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class AioTestCaseAutomationStatusDto {

    private Integer id;
    private String name;
    private String description;
    private Integer displayOrder;
    private AioTestAutomationStatusType testAutomationStatusType;
    private Boolean isAutomationComplete;
    private Long updatedDate;

    @JsonProperty("ID")
    public Integer getId() {
        return id;
    }

    @JsonProperty("ID")
    public AioTestCaseAutomationStatusDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public AioTestCaseAutomationStatusDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AioTestCaseAutomationStatusDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public AioTestCaseAutomationStatusDto setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
        return this;
    }

    public AioTestAutomationStatusType getTestAutomationStatusType() {
        return testAutomationStatusType;
    }

    public AioTestCaseAutomationStatusDto setTestAutomationStatusType(AioTestAutomationStatusType testAutomationStatusType) {
        this.testAutomationStatusType = testAutomationStatusType;
        return this;
    }

    public Boolean getAutomationComplete() {
        return isAutomationComplete;
    }

    public AioTestCaseAutomationStatusDto setAutomationComplete(Boolean automationComplete) {
        isAutomationComplete = automationComplete;
        return this;
    }

    public Long getUpdatedDate() {
        return updatedDate;
    }

    public AioTestCaseAutomationStatusDto setUpdatedDate(Long updatedDate) {
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
        AioTestCaseAutomationStatusDto that = (AioTestCaseAutomationStatusDto) o;

        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && Objects.equals(displayOrder, that.displayOrder)
                && testAutomationStatusType == that.testAutomationStatusType
                && Objects.equals(isAutomationComplete, that.isAutomationComplete)
                && Objects.equals(updatedDate, that.updatedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, displayOrder, testAutomationStatusType, isAutomationComplete, updatedDate);
    }

    @Override
    public String toString() {
        return String.format(
                "AioTestCaseAutomationStatusDto {%n id=%s,%n name=%s,%n description=%s,%n displayOrder=%s,%n statusType=%s,%n isAutomationComplete=%s,%n updatedDate=%s%n}",
                id,
                name,
                description,
                displayOrder,
                testAutomationStatusType,
                isAutomationComplete,
                updatedDate
        );
    }
}
