package com.example.autotest.backend.rest.jira.aio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AioConfigurationDto implements Serializable {

    private Integer jiraProjectId;
    private String headwayKey;
    private List<AioEntityDto> entityType = Collections.emptyList();
    private List<AioTestCasePriorityDto> testPriority = Collections.emptyList();
    private List<AioTestCaseTypeDto> testType = Collections.emptyList();
    private List<AioTestCaseStatusDto> testStatus = Collections.emptyList();
    private List<AioTestRunStatusDto> testRunStatus = Collections.emptyList();
    private List<AioTestRunStepStatusDto> testRunStepStatus = Collections.emptyList();
    private List<AioTestCaseAutomationStatusDto> testAutomationStatus = Collections.emptyList();
    private List<AioTestCaseScriptTypeDto> testScriptType = Collections.emptyList();
    private transient List<Object> customFields = Collections.emptyList();
    private List<AioCustomFieldTypeDto> customFieldTypes = Collections.emptyList();
    private List<AioCustomFieldJiraTypeDto> customFieldJiraTypes = Collections.emptyList();
    private List<AioFieldDto> disabledFields = Collections.emptyList();
    private List<AioFieldDto> requiredFields = Collections.emptyList();
    private transient Object userEmailPreferences = new Object();

    @JsonProperty("jiraProjectID")
    public Integer getJiraProjectId() {
        return jiraProjectId;
    }

    @JsonProperty("jiraProjectID")
    public AioConfigurationDto setJiraProjectId(Integer jiraProjectId) {
        this.jiraProjectId = jiraProjectId;
        return this;
    }

    public String getHeadwayKey() {
        return headwayKey;
    }

    public AioConfigurationDto setHeadwayKey(String headwayKey) {
        this.headwayKey = headwayKey;
        return this;
    }

    public List<AioEntityDto> getEntityType() {
        return entityType;
    }

    public AioConfigurationDto setEntityType(List<AioEntityDto> entityType) {
        this.entityType = entityType;
        return this;
    }

    public List<AioTestCasePriorityDto> getTestPriority() {
        return testPriority;
    }

    public AioConfigurationDto setTestPriority(List<AioTestCasePriorityDto> testPriority) {
        this.testPriority = testPriority;
        return this;
    }

    public List<AioTestCaseTypeDto> getTestType() {
        return testType;
    }

    public AioConfigurationDto setTestType(List<AioTestCaseTypeDto> testType) {
        this.testType = testType;
        return this;
    }

    public List<AioTestCaseStatusDto> getTestStatus() {
        return testStatus;
    }

    public AioConfigurationDto setTestStatus(List<AioTestCaseStatusDto> testStatus) {
        this.testStatus = testStatus;
        return this;
    }

    public List<AioTestRunStatusDto> getTestRunStatus() {
        return testRunStatus;
    }

    public AioConfigurationDto setTestRunStatus(List<AioTestRunStatusDto> testRunStatus) {
        this.testRunStatus = testRunStatus;
        return this;
    }

    public List<AioTestRunStepStatusDto> getTestRunStepStatus() {
        return testRunStepStatus;
    }

    public AioConfigurationDto setTestRunStepStatus(List<AioTestRunStepStatusDto> testRunStepStatus) {
        this.testRunStepStatus = testRunStepStatus;
        return this;
    }

    public List<AioTestCaseAutomationStatusDto> getTestAutomationStatus() {
        return testAutomationStatus;
    }

    public AioConfigurationDto setTestAutomationStatus(List<AioTestCaseAutomationStatusDto> testAutomationStatus) {
        this.testAutomationStatus = testAutomationStatus;
        return this;
    }

    public List<AioTestCaseScriptTypeDto> getTestScriptType() {
        return testScriptType;
    }

    public AioConfigurationDto setTestScriptType(List<AioTestCaseScriptTypeDto> testScriptType) {
        this.testScriptType = testScriptType;
        return this;
    }

    public List<Object> getCustomFields() {
        return customFields;
    }

    public AioConfigurationDto setCustomFields(List<Object> customFields) {
        this.customFields = customFields;
        return this;
    }

    public List<AioCustomFieldTypeDto> getCustomFieldTypes() {
        return customFieldTypes;
    }

    public AioConfigurationDto setCustomFieldTypes(List<AioCustomFieldTypeDto> customFieldTypes) {
        this.customFieldTypes = customFieldTypes;
        return this;
    }

    public List<AioCustomFieldJiraTypeDto> getCustomFieldJiraTypes() {
        return customFieldJiraTypes;
    }

    public AioConfigurationDto setCustomFieldJiraTypes(List<AioCustomFieldJiraTypeDto> customFieldJiraTypes) {
        this.customFieldJiraTypes = customFieldJiraTypes;
        return this;
    }

    public List<AioFieldDto> getDisabledFields() {
        return disabledFields;
    }

    public AioConfigurationDto setDisabledFields(List<AioFieldDto> disabledFields) {
        this.disabledFields = disabledFields;
        return this;
    }

    public List<AioFieldDto> getRequiredFields() {
        return requiredFields;
    }

    public AioConfigurationDto setRequiredFields(List<AioFieldDto> requiredFields) {
        this.requiredFields = requiredFields;
        return this;
    }

    public Object getUserEmailPreferences() {
        return userEmailPreferences;
    }

    public AioConfigurationDto setUserEmailPreferences(Object userEmailPreferences) {
        this.userEmailPreferences = userEmailPreferences;
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
        AioConfigurationDto that = (AioConfigurationDto) o;

        return Objects.equals(jiraProjectId, that.jiraProjectId)
                && Objects.equals(headwayKey, that.headwayKey)
                && Objects.equals(entityType, that.entityType)
                && Objects.equals(testPriority, that.testPriority)
                && Objects.equals(testType, that.testType)
                && Objects.equals(testStatus, that.testStatus)
                && Objects.equals(testRunStatus, that.testRunStatus)
                && Objects.equals(testRunStepStatus, that.testRunStepStatus)
                && Objects.equals(testAutomationStatus, that.testAutomationStatus)
                && Objects.equals(testScriptType, that.testScriptType)
                && Objects.equals(customFields, that.customFields)
                && Objects.equals(customFieldTypes, that.customFieldTypes)
                && Objects.equals(customFieldJiraTypes, that.customFieldJiraTypes)
                && Objects.equals(disabledFields, that.disabledFields)
                && Objects.equals(requiredFields, that.requiredFields)
                && Objects.equals(userEmailPreferences, that.userEmailPreferences);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                jiraProjectId,
                headwayKey,
                entityType,
                testPriority,
                testType,
                testStatus,
                testRunStatus,
                testRunStepStatus,
                testAutomationStatus,
                testScriptType,
                customFields,
                customFieldTypes,
                customFieldJiraTypes,
                disabledFields,
                requiredFields,
                userEmailPreferences
        );
    }

    @Override
    public String toString() {
        return String.format(
                "AioConfigurationDto {%n jiraProjectId=%s,%n headwayKey=%s,%n entityType=%s,%n testPriority=%s,%n testType=%s,%n testStatus=%s,%n testRunStatus=%s,%n testRunStepStatus=%s,%n testAutomationStatus=%s,%n testScriptType=%s,%n customFields=%s,%n customFieldTypes=%s,%n customFieldJiraTypes=%s,%n disabledFields=%s,%n requiredFields=%s,%n userEmailPreferences=%s%n}",
                jiraProjectId,
                headwayKey,
                entityType,
                testPriority,
                testType,
                testStatus,
                testRunStatus,
                testRunStepStatus,
                testAutomationStatus,
                testScriptType,
                customFields,
                customFieldTypes,
                customFieldJiraTypes,
                disabledFields,
                requiredFields,
                userEmailPreferences
        );
    }
}
