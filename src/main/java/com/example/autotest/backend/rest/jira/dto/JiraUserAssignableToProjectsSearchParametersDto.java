package com.example.autotest.backend.rest.jira.dto;

import java.util.Objects;

public class JiraUserAssignableToProjectsSearchParametersDto {

    private String query;
    private String userName;
    private String accountId;
    private String projectKeys;
    private int startAt = 0;
    private int maxResults = 50;

    public String getQuery() {
        return query;
    }

    public JiraUserAssignableToProjectsSearchParametersDto setQuery(String query) {
        this.query = query;
        return this;
    }

    public String getUserName() {
        return userName;
    }

    public JiraUserAssignableToProjectsSearchParametersDto setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public String getAccountId() {
        return accountId;
    }

    public JiraUserAssignableToProjectsSearchParametersDto setAccountId(String accountId) {
        this.accountId = accountId;
        return this;
    }

    public String getProjectKeys() {
        return projectKeys;
    }

    public JiraUserAssignableToProjectsSearchParametersDto setProjectKeys(String projectKeys) {
        this.projectKeys = projectKeys;
        return this;
    }

    public int getStartAt() {
        return startAt;
    }

    public JiraUserAssignableToProjectsSearchParametersDto setStartAt(int startAt) {
        this.startAt = startAt;
        return this;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public JiraUserAssignableToProjectsSearchParametersDto setMaxResults(int maxResults) {
        this.maxResults = maxResults;
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
        JiraUserAssignableToProjectsSearchParametersDto that = (JiraUserAssignableToProjectsSearchParametersDto) o;

        return startAt == that.startAt
                && maxResults == that.maxResults
                && Objects.equals(query, that.query)
                && Objects.equals(userName, that.userName)
                && Objects.equals(accountId, that.accountId)
                && Objects.equals(projectKeys, that.projectKeys);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query, userName, accountId, projectKeys, startAt, maxResults);
    }

    @Override
    public String toString() {
        return String.format(
                "JiraUserAssignableToProjectsSearchParametersDto {%n query=%s,%n userName=%s,%n accountId=%s,%n projectKeys=%s,%n startAt=%s,%n maxResults=%s%n}",
                query,
                userName,
                accountId,
                projectKeys,
                startAt,
                maxResults
        );
    }
}
