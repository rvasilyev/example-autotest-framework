package com.example.autotest.backend.rest.jira.aio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AioTestCaseDetailDto implements Serializable {

    private String key;
    private String title;
    private String description;
    private String ownedById;
    private AioFolderDto folder;
    private Integer testStatusId;
    private Integer testAutomationStatusId;
    private Boolean autoStatusUpdDate;
    private String automationOwnerId;
    private String automationKey;
    private Integer testPriorityId;
    private Integer testTypeId;
    private Integer jiraComponentId;
    private Integer jiraReleaseId;
    private Integer testScriptTypeId;
    private Long createdDate;
    private Long updatedDate;
    private Integer estimatedEffort;
    private List<AioTagWrapperDto> tags = Collections.emptyList();
    private List<AioRequirementDto> requirements = Collections.emptyList();
    private transient Object customFieldValues = new Object(); // при null получаем ошибку 500 и некорректный ответ, но кейс создается

    public String getKey() {
        return key;
    }

    public AioTestCaseDetailDto setKey(String key) {
        this.key = key;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public AioTestCaseDetailDto setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public AioTestCaseDetailDto setDescription(String description) {
        this.description = description;
        return this;
    }

    @JsonProperty("ownedByID")
    public String getOwnedById() {
        return ownedById;
    }

    @JsonProperty("ownedByID")
    public AioTestCaseDetailDto setOwnedById(String ownedById) {
        this.ownedById = ownedById;
        return this;
    }

    public AioFolderDto getFolder() {
        return folder;
    }

    public AioTestCaseDetailDto setFolder(AioFolderDto folder) {
        this.folder = folder;
        return this;
    }

    @JsonProperty("testStatusID")
    public Integer getTestStatusId() {
        return testStatusId;
    }

    @JsonProperty("testStatusID")
    public AioTestCaseDetailDto setTestStatusId(Integer testStatusId) {
        this.testStatusId = testStatusId;
        return this;
    }

    @JsonProperty("testAutomationStatusID")
    public Integer getTestAutomationStatusId() {
        return testAutomationStatusId;
    }

    @JsonProperty("testAutomationStatusID")
    public AioTestCaseDetailDto setTestAutomationStatusId(Integer testAutomationStatusId) {
        this.testAutomationStatusId = testAutomationStatusId;
        return this;
    }

    public Boolean getAutoStatusUpdDate() {
        return autoStatusUpdDate;
    }

    public AioTestCaseDetailDto setAutoStatusUpdDate(Boolean autoStatusUpdDate) {
        this.autoStatusUpdDate = autoStatusUpdDate;
        return this;
    }

    @JsonProperty("automationOwnerID")
    public String getAutomationOwnerId() {
        return automationOwnerId;
    }

    @JsonProperty("automationOwnerID")
    public AioTestCaseDetailDto setAutomationOwnerId(String automationOwnerId) {
        this.automationOwnerId = automationOwnerId;
        return this;
    }

    public String getAutomationKey() {
        return automationKey;
    }

    public AioTestCaseDetailDto setAutomationKey(String automationKey) {
        this.automationKey = automationKey;
        return this;
    }

    @JsonProperty("testPriorityID")
    public Integer getTestPriorityId() {
        return testPriorityId;
    }

    @JsonProperty("testPriorityID")
    public AioTestCaseDetailDto setTestPriorityId(Integer testPriorityId) {
        this.testPriorityId = testPriorityId;
        return this;
    }

    @JsonProperty("testTypeID")
    public Integer getTestTypeId() {
        return testTypeId;
    }

    @JsonProperty("testTypeID")
    public AioTestCaseDetailDto setTestTypeId(Integer testTypeId) {
        this.testTypeId = testTypeId;
        return this;
    }

    @JsonProperty("jiraComponentID")
    public Integer getJiraComponentId() {
        return jiraComponentId;
    }

    @JsonProperty("jiraComponentID")
    public AioTestCaseDetailDto setJiraComponentId(Integer jiraComponentId) {
        this.jiraComponentId = jiraComponentId;
        return this;
    }

    @JsonProperty("jiraReleaseID")
    public Integer getJiraReleaseId() {
        return jiraReleaseId;
    }

    @JsonProperty("jiraReleaseID")
    public AioTestCaseDetailDto setJiraReleaseId(Integer jiraReleaseId) {
        this.jiraReleaseId = jiraReleaseId;
        return this;
    }

    @JsonProperty("testScriptTypeID")
    public Integer getTestScriptTypeId() {
        return testScriptTypeId;
    }

    @JsonProperty("testScriptTypeID")
    public AioTestCaseDetailDto setTestScriptTypeId(Integer testScriptTypeId) {
        this.testScriptTypeId = testScriptTypeId;
        return this;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public AioTestCaseDetailDto setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public Long getUpdatedDate() {
        return updatedDate;
    }

    public AioTestCaseDetailDto setUpdatedDate(Long updatedDate) {
        this.updatedDate = updatedDate;
        return this;
    }

    public Integer getEstimatedEffort() {
        return estimatedEffort;
    }

    public AioTestCaseDetailDto setEstimatedEffort(Integer estimatedEffort) {
        this.estimatedEffort = estimatedEffort;
        return this;
    }

    public List<AioTagWrapperDto> getTags() {
        return tags;
    }

    public AioTestCaseDetailDto setTags(List<AioTagWrapperDto> tags) {
        this.tags = tags;
        return this;
    }

    public List<AioRequirementDto> getRequirements() {
        return requirements;
    }

    public AioTestCaseDetailDto setRequirements(List<AioRequirementDto> requirements) {
        this.requirements = requirements;
        return this;
    }

    public Object getCustomFieldValues() {
        return customFieldValues;
    }

    public AioTestCaseDetailDto setCustomFieldValues(Object customFieldValues) {
        this.customFieldValues = customFieldValues;
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
        AioTestCaseDetailDto that = (AioTestCaseDetailDto) o;

        return Objects.equals(key, that.key)
                && Objects.equals(title, that.title)
                && Objects.equals(description, that.description)
                && Objects.equals(ownedById, that.ownedById)
                && Objects.equals(folder, that.folder)
                && Objects.equals(testStatusId, that.testStatusId)
                && Objects.equals(testAutomationStatusId, that.testAutomationStatusId)
                && Objects.equals(autoStatusUpdDate, that.autoStatusUpdDate)
                && Objects.equals(automationOwnerId, that.automationOwnerId)
                && Objects.equals(automationKey, that.automationKey)
                && Objects.equals(testPriorityId, that.testPriorityId)
                && Objects.equals(testTypeId, that.testTypeId)
                && Objects.equals(jiraComponentId, that.jiraComponentId)
                && Objects.equals(jiraReleaseId, that.jiraReleaseId)
                && Objects.equals(testScriptTypeId, that.testScriptTypeId)
                && Objects.equals(createdDate, that.createdDate)
                && Objects.equals(updatedDate, that.updatedDate)
                && Objects.equals(estimatedEffort, that.estimatedEffort)
                && Objects.equals(tags, that.tags)
                && Objects.equals(requirements, that.requirements)
                && Objects.equals(customFieldValues, that.customFieldValues);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key,
                title,
                description,
                ownedById,
                folder,
                testStatusId,
                testAutomationStatusId,
                autoStatusUpdDate,
                automationOwnerId,
                automationKey,
                testPriorityId,
                testTypeId,
                jiraComponentId,
                jiraReleaseId,
                testScriptTypeId,
                createdDate,
                updatedDate,
                estimatedEffort,
                tags,
                requirements,
                customFieldValues);
    }

    @Override
    public String toString() {
        return String.format(
                "AioTestCaseDetailDto {%n key=%s,%n title=%s,%n description=%s,%n ownedById=%s,%n folder=%s,%n testStatusId=%s,%n testAutomationStatusId=%s,%n autoStatusUpdDate=%s,%n automationOwnerId=%s,%n automationKey=%s,%n testPriorityId=%s,%n testTypeId=%s,%n jiraComponentId=%s,%n jiraReleaseId=%s,%n testScriptTypeId=%s,%n createdDate=%s,%n updatedDate=%s,%n estimatedEffort=%s,%n tags=%s,%n requirements=%s,%n customFieldValues=%s%n}",
                key,
                title,
                description,
                ownedById,
                folder,
                testStatusId,
                testAutomationStatusId,
                autoStatusUpdDate,
                automationOwnerId,
                automationKey,
                testPriorityId,
                testTypeId,
                jiraComponentId,
                jiraReleaseId,
                testScriptTypeId,
                createdDate,
                updatedDate,
                estimatedEffort,
                tags,
                requirements,
                customFieldValues
        );
    }
}
