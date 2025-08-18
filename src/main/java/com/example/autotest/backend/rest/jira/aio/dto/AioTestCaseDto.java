package com.example.autotest.backend.rest.jira.aio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class AioTestCaseDto implements Serializable {

    private Integer id;
    private Integer jiraProjectId;
    private AioPermissionDto permission;
    private AioTestCaseDetailDto detail;
    private String precondition;
    private List<AioStepDto> steps = Collections.emptyList();
    private List<AioAttachmentDto> descriptionAttachments = Collections.emptyList();
    private List<AioAttachmentDto> preconditionAttachments = Collections.emptyList();

    @JsonProperty("ID")
    public Integer getId() {
        return id;
    }

    @JsonProperty("ID")
    public AioTestCaseDto setId(Integer id) {
        this.id = id;
        return this;
    }

    @JsonProperty("jiraProjectID")
    public Integer getJiraProjectId() {
        return jiraProjectId;
    }

    @JsonProperty("jiraProjectID")
    public AioTestCaseDto setJiraProjectId(Integer jiraProjectId) {
        this.jiraProjectId = jiraProjectId;
        return this;
    }

    public AioPermissionDto getPermission() {
        return permission;
    }

    public AioTestCaseDto setPermission(AioPermissionDto permission) {
        this.permission = permission;
        return this;
    }

    public AioTestCaseDetailDto getDetail() {
        return detail;
    }

    public AioTestCaseDto setDetail(AioTestCaseDetailDto detail) {
        this.detail = detail;
        return this;
    }

    public String getPrecondition() {
        return precondition;
    }

    public AioTestCaseDto setPrecondition(String precondition) {
        this.precondition = precondition;
        return this;
    }

    public List<AioStepDto> getSteps() {
        return steps;
    }

    public AioTestCaseDto setSteps(List<AioStepDto> steps) {
        this.steps = steps;
        return this;
    }

    public List<AioAttachmentDto> getDescriptionAttachments() {
        return descriptionAttachments;
    }

    public AioTestCaseDto setDescriptionAttachments(List<AioAttachmentDto> descriptionAttachments) {
        this.descriptionAttachments = descriptionAttachments;
        return this;
    }

    public List<AioAttachmentDto> getPreconditionAttachments() {
        return preconditionAttachments;
    }

    public AioTestCaseDto setPreconditionAttachments(List<AioAttachmentDto> preconditionAttachments) {
        this.preconditionAttachments = preconditionAttachments;
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
        AioTestCaseDto that = (AioTestCaseDto) o;

        return Objects.equals(id, that.id)
                && Objects.equals(jiraProjectId, that.jiraProjectId)
                && Objects.equals(permission, that.permission)
                && Objects.equals(detail, that.detail)
                && Objects.equals(precondition, that.precondition)
                && Objects.equals(steps, that.steps)
                && Objects.equals(descriptionAttachments, that.descriptionAttachments)
                && Objects.equals(preconditionAttachments, that.preconditionAttachments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, jiraProjectId, permission, detail, precondition, steps, descriptionAttachments, preconditionAttachments);
    }

    @Override
    public String toString() {
        return String.format(
                "AioTestCaseDto {%n id=%s,%n jiraProjectId=%s,%n permission=%s,%n detail=%s,%n precondition=%s,%n steps=%s,%n descriptionAttachments=%s,%n preconditionAttachments=%s%n}",
                id,
                jiraProjectId,
                permission,
                detail,
                precondition,
                steps,
                descriptionAttachments,
                preconditionAttachments
        );
    }
}
