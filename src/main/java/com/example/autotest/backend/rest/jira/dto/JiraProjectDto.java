package com.example.autotest.backend.rest.jira.dto;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class JiraProjectDto {

    private String expand;
    private String self;
    private String id;
    private String key;
    private String name;
    private String description;
    private Object lead;
    private List<Object> components = Collections.emptyList();
    private List<Object> issueTypes = Collections.emptyList();
    private String assigneeType;
    private List<Object> versions = Collections.emptyList();
    private Object roles;
    private Object avatarUrls;
    private Object projectCategory;
    private String projectTypeKey;
    private Boolean archived;

    public String getExpand() {
        return expand;
    }

    public JiraProjectDto setExpand(String expand) {
        this.expand = expand;
        return this;
    }

    public String getSelf() {
        return self;
    }

    public JiraProjectDto setSelf(String self) {
        this.self = self;
        return this;
    }

    public String getId() {
        return id;
    }

    public JiraProjectDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getKey() {
        return key;
    }

    public JiraProjectDto setKey(String key) {
        this.key = key;
        return this;
    }

    public String getName() {
        return name;
    }

    public JiraProjectDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public JiraProjectDto setDescription(String description) {
        this.description = description;
        return this;
    }

    public Object getLead() {
        return lead;
    }

    public JiraProjectDto setLead(Object lead) {
        this.lead = lead;
        return this;
    }

    public List<Object> getComponents() {
        return components;
    }

    public JiraProjectDto setComponents(List<Object> components) {
        this.components = components;
        return this;
    }

    public List<Object> getIssueTypes() {
        return issueTypes;
    }

    public JiraProjectDto setIssueTypes(List<Object> issueTypes) {
        this.issueTypes = issueTypes;
        return this;
    }

    public String getAssigneeType() {
        return assigneeType;
    }

    public JiraProjectDto setAssigneeType(String assigneeType) {
        this.assigneeType = assigneeType;
        return this;
    }

    public List<Object> getVersions() {
        return versions;
    }

    public JiraProjectDto setVersions(List<Object> versions) {
        this.versions = versions;
        return this;
    }

    public Object getRoles() {
        return roles;
    }

    public JiraProjectDto setRoles(Object roles) {
        this.roles = roles;
        return this;
    }

    public Object getAvatarUrls() {
        return avatarUrls;
    }

    public JiraProjectDto setAvatarUrls(Object avatarUrls) {
        this.avatarUrls = avatarUrls;
        return this;
    }

    public Object getProjectCategory() {
        return projectCategory;
    }

    public JiraProjectDto setProjectCategory(Object projectCategory) {
        this.projectCategory = projectCategory;
        return this;
    }

    public String getProjectTypeKey() {
        return projectTypeKey;
    }

    public JiraProjectDto setProjectTypeKey(String projectTypeKey) {
        this.projectTypeKey = projectTypeKey;
        return this;
    }

    public Boolean getArchived() {
        return archived;
    }

    public JiraProjectDto setArchived(Boolean archived) {
        this.archived = archived;
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
        JiraProjectDto that = (JiraProjectDto) o;

        return Objects.equals(expand, that.expand)
                && Objects.equals(self, that.self)
                && Objects.equals(id, that.id)
                && Objects.equals(key, that.key)
                && Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && Objects.equals(lead, that.lead)
                && Objects.equals(components, that.components)
                && Objects.equals(issueTypes, that.issueTypes)
                && Objects.equals(assigneeType, that.assigneeType)
                && Objects.equals(versions, that.versions)
                && Objects.equals(roles, that.roles)
                && Objects.equals(avatarUrls, that.avatarUrls)
                && Objects.equals(projectTypeKey, that.projectTypeKey)
                && Objects.equals(archived, that.archived);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expand,
                self,
                id,
                key,
                name,
                description,
                lead,
                components,
                issueTypes,
                assigneeType,
                versions,
                roles,
                avatarUrls,
                projectTypeKey,
                archived);
    }

    @Override
    public String toString() {
        return String.format(
                "JiraProjectDto {%n expand=%s,%n self=%s,%n id=%s,%n key=%s,%n name=%s,%n description=%s,%n lead=%s,%n components=%s,%n issueTypes=%s,%n assigneeType=%s,%n versions=%s,%n roles=%s,%n avatarUrls=%s,%n projectTypeKey=%s,%n archived=%s%n}",
                expand,
                self,
                id,
                key,
                name,
                description,
                lead,
                components,
                issueTypes,
                assigneeType,
                versions,
                roles,
                avatarUrls,
                projectTypeKey,
                archived
        );
    }
}
