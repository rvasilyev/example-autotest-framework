package com.example.autotest.backend.rest.jira.aio.dto;

import com.example.autotest.backend.rest.jira.aio.type.AioTestStatusType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class AioTestCaseStatusDto implements Serializable {

    private Integer id;
    private String name;
    private String description;
    private AioTestStatusType statusType;
    private Integer displayOrder;
    private String color;
    private Boolean canBeAddedToTestCycle;
    private Boolean isVisibleToEveryone;
    private Boolean canBeAddedAsRefStep;
    private Long updatedDate;
    private transient Object deletionStatus;
    private AioTestStatusType testStatusType;

    @JsonProperty("ID")
    public Integer getId() {
        return id;
    }

    @JsonProperty("ID")
    public AioTestCaseStatusDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public AioTestCaseStatusDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AioTestCaseStatusDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public AioTestStatusType getStatusType() {
        return statusType;
    }

    public AioTestCaseStatusDto setStatusType(AioTestStatusType statusType) {
        this.statusType = statusType;
        return this;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public AioTestCaseStatusDto setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
        return this;
    }

    public String getColor() {
        return color;
    }

    public AioTestCaseStatusDto setColor(String color) {
        this.color = color;
        return this;
    }

    public Boolean getCanBeAddedToTestCycle() {
        return canBeAddedToTestCycle;
    }

    public AioTestCaseStatusDto setCanBeAddedToTestCycle(Boolean canBeAddedToTestCycle) {
        this.canBeAddedToTestCycle = canBeAddedToTestCycle;
        return this;
    }

    public Boolean getVisibleToEveryone() {
        return isVisibleToEveryone;
    }

    public AioTestCaseStatusDto setVisibleToEveryone(Boolean visibleToEveryone) {
        isVisibleToEveryone = visibleToEveryone;
        return this;
    }

    public Boolean getCanBeAddedAsRefStep() {
        return canBeAddedAsRefStep;
    }

    public AioTestCaseStatusDto setCanBeAddedAsRefStep(Boolean canBeAddedAsRefStep) {
        this.canBeAddedAsRefStep = canBeAddedAsRefStep;
        return this;
    }

    public Long getUpdatedDate() {
        return updatedDate;
    }

    public AioTestCaseStatusDto setUpdatedDate(Long updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public Object getDeletionStatus() {
        return deletionStatus;
    }

    public AioTestCaseStatusDto setDeletionStatus(Object deletionStatus) {
        this.deletionStatus = deletionStatus;
        return this;
    }

    public AioTestStatusType getTestStatusType() {
        return testStatusType;
    }

    public AioTestCaseStatusDto setTestStatusType(AioTestStatusType testStatusType) {
        this.testStatusType = testStatusType;
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
        AioTestCaseStatusDto that = (AioTestCaseStatusDto) o;
        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && statusType == that.statusType
                && Objects.equals(displayOrder, that.displayOrder)
                && Objects.equals(color, that.color)
                && Objects.equals(canBeAddedToTestCycle, that.canBeAddedToTestCycle)
                && Objects.equals(isVisibleToEveryone, that.isVisibleToEveryone)
                && Objects.equals(canBeAddedAsRefStep, that.canBeAddedAsRefStep)
                && Objects.equals(updatedDate, that.updatedDate)
                && Objects.equals(deletionStatus, that.deletionStatus)
                && testStatusType == that.testStatusType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id,
                name,
                description,
                statusType,
                displayOrder,
                color,
                canBeAddedToTestCycle,
                isVisibleToEveryone,
                canBeAddedAsRefStep,
                updatedDate,
                deletionStatus,
                testStatusType);
    }

    @Override
    public String toString() {
        return String.format(
                "AioTestCaseStatusDto {%n id=%s,%n name=%s,%n description=%s,%n statusType=%s,%n displayOrder=%s,%n color=%s,%n canBeAddedToTestCycle=%s,%n isVisibleToEveryone=%s,%n canBeAddedAsRefStep=%s,%n updatedDate=%s,%n deletionStatus=%s,%n testStatusType=%s%n}",
                id,
                name,
                description,
                statusType,
                displayOrder,
                color,
                canBeAddedToTestCycle,
                isVisibleToEveryone,
                canBeAddedAsRefStep,
                updatedDate,
                deletionStatus,
                testStatusType
        );
    }
}
