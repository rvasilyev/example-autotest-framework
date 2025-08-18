package com.example.autotest.backend.rest.jira.aio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class AioRequirementBulkDto implements Serializable {

    private Integer testCaseId;
    private List<String> idList;

    @JsonProperty("testCaseID")
    public Integer getTestCaseId() {
        return testCaseId;
    }

    @JsonProperty("testCaseID")
    public AioRequirementBulkDto setTestCaseId(Integer testCaseId) {
        this.testCaseId = testCaseId;
        return this;
    }

    public List<String> getIdList() {
        return idList;
    }

    public AioRequirementBulkDto setIdList(List<String> idList) {
        this.idList = idList;
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
        AioRequirementBulkDto that = (AioRequirementBulkDto) o;

        return Objects.equals(testCaseId, that.testCaseId) && Objects.equals(idList, that.idList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(testCaseId, idList);
    }

    @Override
    public String toString() {
        return String.format("AioRequirementBulkDto {%n testCaseId=%s,%n idList=%s%n}", testCaseId, idList);
    }
}
