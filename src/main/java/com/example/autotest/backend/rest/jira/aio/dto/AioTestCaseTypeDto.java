package com.example.autotest.backend.rest.jira.aio.dto;

import com.example.autotest.backend.rest.jira.aio.type.AioTestCaseType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class AioTestCaseTypeDto implements Serializable {

    private Integer id;
    private AioTestCaseType name;
    private Integer displayOrder;
    private Long createdDate;
    private Long updatedDate;
    private Boolean isArchived;
    private transient Object deletionStatus;

    @JsonProperty("ID")
    public Integer getId() {
        return id;
    }

    @JsonProperty("ID")
    public AioTestCaseTypeDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public AioTestCaseType getName() {
        return name;
    }

    public AioTestCaseTypeDto setName(AioTestCaseType name) {
        this.name = name;
        return this;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public AioTestCaseTypeDto setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
        return this;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public AioTestCaseTypeDto setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public Long getUpdatedDate() {
        return updatedDate;
    }

    public AioTestCaseTypeDto setUpdatedDate(Long updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public Boolean getArchived() {
        return isArchived;
    }

    public AioTestCaseTypeDto setArchived(Boolean archived) {
        isArchived = archived;
        return this;
    }

    public Object getDeletionStatus() {
        return deletionStatus;
    }

    public AioTestCaseTypeDto setDeletionStatus(Object deletionStatus) {
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
        AioTestCaseTypeDto that = (AioTestCaseTypeDto) o;

        return Objects.equals(id, that.id)
                && name == that.name
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
                "AioTestCaseTypeDto {%n id=%s,%n name=%s,%n displayOrder=%s,%n createdDate=%s,%n updatedDate=%s,%n isArchived=%s,%n deletionStatus=%s%n}",
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
