package com.example.autotest.backend.rest.jira.dto;

import java.util.Objects;

public class JiraUserDto {

    private String self;
    private String key;
    private String name;
    private String emailAddress;
    private Object avatarUrls;
    private String displayName;
    private Boolean active;
    private Boolean deleted;
    private String timeZone;
    private String locale;

    public String getSelf() {
        return self;
    }

    public JiraUserDto setSelf(String self) {
        this.self = self;
        return this;
    }

    public String getKey() {
        return key;
    }

    public JiraUserDto setKey(String key) {
        this.key = key;
        return this;
    }

    public String getName() {
        return name;
    }

    public JiraUserDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public JiraUserDto setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;
    }

    public Object getAvatarUrls() {
        return avatarUrls;
    }

    public JiraUserDto setAvatarUrls(Object avatarUrls) {
        this.avatarUrls = avatarUrls;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public JiraUserDto setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public JiraUserDto setActive(Boolean active) {
        this.active = active;
        return this;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public JiraUserDto setDeleted(Boolean deleted) {
        this.deleted = deleted;
        return this;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public JiraUserDto setTimeZone(String timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    public String getLocale() {
        return locale;
    }

    public JiraUserDto setLocale(String locale) {
        this.locale = locale;
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
        JiraUserDto that = (JiraUserDto) o;
        return Objects.equals(self, that.self)
                && Objects.equals(key, that.key)
                && Objects.equals(name, that.name)
                && Objects.equals(emailAddress, that.emailAddress)
                && Objects.equals(avatarUrls, that.avatarUrls)
                && Objects.equals(displayName, that.displayName)
                && Objects.equals(active, that.active)
                && Objects.equals(deleted, that.deleted)
                && Objects.equals(timeZone, that.timeZone)
                && Objects.equals(locale, that.locale);
    }

    @Override
    public int hashCode() {
        return Objects.hash(self, key, name, emailAddress, avatarUrls, displayName, active, deleted, timeZone, locale);
    }

    @Override
    public String toString() {
        return String.format(
                "JiraUserDto {%n self=%s,%n key=%s,%n name=%s,%n emailAddress=%s,%n avatarUrls=%s,%n displayName=%s,%n active=%s,%n deleted=%s,%n timeZone=%s,%n locale=%s%n}",
                self,
                key,
                name,
                emailAddress,
                avatarUrls,
                displayName,
                active,
                deleted,
                timeZone,
                locale
        );
    }
}
