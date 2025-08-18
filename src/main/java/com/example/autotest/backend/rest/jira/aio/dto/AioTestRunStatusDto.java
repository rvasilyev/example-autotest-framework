package com.example.autotest.backend.rest.jira.aio.dto;

import com.example.autotest.backend.rest.jira.aio.type.AioRunStatusType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class AioTestRunStatusDto implements Serializable {

    private Integer id;
    private String name;
    private String description;
    private AioRunStatusType statusType;
    private Integer displayOrder;
    private String color;
    private AioRunStatusType coverageType;
    private Boolean isExecCompleted;
    private transient Object deletionStatus;
    private Long createdDate;
    private Long updatedDate;
    private AioRunStatusType testRunStatusType;
    private AioRunStatusType testCoverageType;

    @JsonProperty("ID")
    public Integer getId() {
        return id;
    }

    @JsonProperty("ID")
    public AioTestRunStatusDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public AioTestRunStatusDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AioTestRunStatusDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public AioRunStatusType getStatusType() {
        return statusType;
    }

    public AioTestRunStatusDto setStatusType(AioRunStatusType statusType) {
        this.statusType = statusType;
        return this;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public AioTestRunStatusDto setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
        return this;
    }

    public String getColor() {
        return color;
    }

    public AioTestRunStatusDto setColor(String color) {
        this.color = color;
        return this;
    }

    public AioRunStatusType getCoverageType() {
        return coverageType;
    }

    public AioTestRunStatusDto setCoverageType(AioRunStatusType coverageType) {
        this.coverageType = coverageType;
        return this;
    }

    public Boolean getExecCompleted() {
        return isExecCompleted;
    }

    public AioTestRunStatusDto setExecCompleted(Boolean execCompleted) {
        isExecCompleted = execCompleted;
        return this;
    }

    public Object getDeletionStatus() {
        return deletionStatus;
    }

    public AioTestRunStatusDto setDeletionStatus(Object deletionStatus) {
        this.deletionStatus = deletionStatus;
        return this;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public AioTestRunStatusDto setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public Long getUpdatedDate() {
        return updatedDate;
    }

    public AioTestRunStatusDto setUpdatedDate(Long updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public AioRunStatusType getTestRunStatusType() {
        return testRunStatusType;
    }

    public AioTestRunStatusDto setTestRunStatusType(AioRunStatusType testRunStatusType) {
        this.testRunStatusType = testRunStatusType;
        return this;
    }

    public AioRunStatusType getTestCoverageType() {
        return testCoverageType;
    }

    public AioTestRunStatusDto setTestCoverageType(AioRunStatusType testCoverageType) {
        this.testCoverageType = testCoverageType;
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
        AioTestRunStatusDto that = (AioTestRunStatusDto) o;

        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && statusType == that.statusType
                && Objects.equals(displayOrder, that.displayOrder)
                && Objects.equals(color, that.color)
                && coverageType == that.coverageType
                && Objects.equals(isExecCompleted, that.isExecCompleted)
                && Objects.equals(deletionStatus, that.deletionStatus)
                && Objects.equals(createdDate, that.createdDate)
                && Objects.equals(updatedDate, that.updatedDate)
                && testRunStatusType == that.testRunStatusType
                && testCoverageType == that.testCoverageType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,
                name,
                description,
                statusType,
                displayOrder,
                color,
                coverageType,
                isExecCompleted,
                deletionStatus,
                createdDate,
                updatedDate,
                testRunStatusType,
                testCoverageType);
    }

    @Override
    public String toString() {
        return String.format(
                "AioTestRunStatusDto {%n id=%s,%n name=%s,%n description=%s,%n statusType=%s,%n displayOrder=%s,%n color=%s,%n coverageType=%s,%n isExecCompleted=%s,%n deletionStatus=%s,%n createdDate=%s,%n updatedDate=%s,%n testRunStatusType=%s,%n testCoverageType=%s%n}",
                id,
                name,
                description,
                statusType,
                displayOrder,
                color,
                coverageType,
                isExecCompleted,
                deletionStatus,
                createdDate,
                updatedDate,
                testRunStatusType,
                testCoverageType
        );
    }
}
