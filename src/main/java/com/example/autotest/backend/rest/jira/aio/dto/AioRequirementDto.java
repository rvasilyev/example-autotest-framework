package com.example.autotest.backend.rest.jira.aio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class AioRequirementDto {

    private Integer id;
    private Object test;
    private Integer requirementId;

    @JsonProperty("ID")
    public Integer getId() {
        return id;
    }

    @JsonProperty("ID")
    public AioRequirementDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public Object getTest() {
        return test;
    }

    public AioRequirementDto setTest(Object test) {
        this.test = test;
        return this;
    }

    @JsonProperty("requirementID")
    public Integer getRequirementId() {
        return requirementId;
    }

    @JsonProperty("requirementID")
    public AioRequirementDto setRequirementId(Integer requirementId) {
        this.requirementId = requirementId;
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
        AioRequirementDto that = (AioRequirementDto) o;

        return Objects.equals(id, that.id) && Objects.equals(test, that.test) && Objects.equals(requirementId, that.requirementId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, test, requirementId);
    }

    @Override
    public String toString() {
        return String.format("AioRequirementDto {%n id=%s,%n test=%s,%n requirementId=%s%n}", id, test, requirementId);
    }
}
