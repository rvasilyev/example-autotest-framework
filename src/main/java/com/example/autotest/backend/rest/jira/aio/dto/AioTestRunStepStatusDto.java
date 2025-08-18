package com.example.autotest.backend.rest.jira.aio.dto;

import com.example.autotest.backend.rest.jira.aio.type.AioRunStatusType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class AioTestRunStepStatusDto implements Serializable {

    private Integer id;
    private String name;
    private String description;
    private AioRunStatusType statusType;
    private Integer displayOrder;
    private String color;
    private transient Object deletionStatus;
    private Long createdDate;
    private Long updatedDate;
    private AioRunStatusType testRunStepStatusType;

    @JsonProperty("ID")
    public Integer getId() {
        return id;
    }

    @JsonProperty("ID")
    public AioTestRunStepStatusDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public AioTestRunStepStatusDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AioTestRunStepStatusDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public AioRunStatusType getStatusType() {
        return statusType;
    }

    public AioTestRunStepStatusDto setStatusType(AioRunStatusType statusType) {
        this.statusType = statusType;
        return this;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public AioTestRunStepStatusDto setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
        return this;
    }

    public String getColor() {
        return color;
    }

    public AioTestRunStepStatusDto setColor(String color) {
        this.color = color;
        return this;
    }

    public Object getDeletionStatus() {
        return deletionStatus;
    }

    public AioTestRunStepStatusDto setDeletionStatus(Object deletionStatus) {
        this.deletionStatus = deletionStatus;
        return this;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public AioTestRunStepStatusDto setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public Long getUpdatedDate() {
        return updatedDate;
    }

    public AioTestRunStepStatusDto setUpdatedDate(Long updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public AioRunStatusType getTestRunStepStatusType() {
        return testRunStepStatusType;
    }

    public AioTestRunStepStatusDto setTestRunStepStatusType(AioRunStatusType testRunStepStatusType) {
        this.testRunStepStatusType = testRunStepStatusType;
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
        AioTestRunStepStatusDto that = (AioTestRunStepStatusDto) o;

        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && statusType == that.statusType
                && Objects.equals(displayOrder, that.displayOrder)
                && Objects.equals(color, that.color)
                && Objects.equals(deletionStatus, that.deletionStatus)
                && Objects.equals(createdDate, that.createdDate)
                && Objects.equals(updatedDate, that.updatedDate)
                && testRunStepStatusType == that.testRunStepStatusType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,
                name,
                description,
                statusType,
                displayOrder,
                color,
                deletionStatus,
                createdDate,
                updatedDate,
                testRunStepStatusType);
    }

    @Override
    public String toString() {
        return String.format(
                "AioTestRunStepStatusDto {%n id=%s,%n name=%s,%n description=%s,%n statusType=%s,%n displayOrder=%s,%n color=%s,%n deletionStatus=%s,%n createdDate=%s,%n updatedDate=%s,%n testRunStepStatusType=%s%n}",
                id,
                name,
                description,
                statusType,
                displayOrder,
                color,
                deletionStatus,
                createdDate,
                updatedDate,
                testRunStepStatusType
        );
    }
}
