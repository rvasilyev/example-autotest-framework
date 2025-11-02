package com.example.autotest.backend.rest.jira.aio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class AioTestCasePriorityDto {

    private Integer id;
    private String name;
    private Integer displayOrder;
    private Long createdDate;
    private Long updatedDate;
    private Boolean isArchived;
    private Object deletionStatus;

    @JsonProperty("ID")
    public Integer getId() {
        return id;
    }

    @JsonProperty("ID")
    public AioTestCasePriorityDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public AioTestCasePriorityDto setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public AioTestCasePriorityDto setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
        return this;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public AioTestCasePriorityDto setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public Long getUpdatedDate() {
        return updatedDate;
    }

    public AioTestCasePriorityDto setUpdatedDate(Long updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public Boolean getArchived() {
        return isArchived;
    }

    public AioTestCasePriorityDto setArchived(Boolean archived) {
        isArchived = archived;
        return this;
    }

    public Object getDeletionStatus() {
        return deletionStatus;
    }

    public AioTestCasePriorityDto setDeletionStatus(Object deletionStatus) {
        this.deletionStatus = deletionStatus;
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
        AioTestCasePriorityDto that = (AioTestCasePriorityDto) o;

        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(displayOrder, that.displayOrder)
                && Objects.equals(createdDate, that.createdDate)
                && Objects.equals(updatedDate, that.updatedDate)
                && Objects.equals(isArchived, that.isArchived)
                && Objects.equals(deletionStatus, that.deletionStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, displayOrder, createdDate, updatedDate, isArchived, deletionStatus);
    }

    @Override
    public String toString() {
        return String.format(
                "AioTestCasePriorityDto {%n id=%s,%n name=%s,%n displayOrder=%s,%n createdDate=%s,%n updatedDate=%s,%n isArchived=%s,%n deletionStatus=%s%n}",
                id,
                name,
                displayOrder,
                createdDate,
                updatedDate,
                isArchived,
                deletionStatus
        );
    }
}
