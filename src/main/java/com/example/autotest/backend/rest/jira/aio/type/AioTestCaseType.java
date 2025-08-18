package com.example.autotest.backend.rest.jira.aio.type;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AioTestCaseType {

    UNIT("Unit"),
    INTEGRATION("Integration"),
    FUNCTIONAL("Functional"),
    API("API"),
    PERFORMANCE("Performance"),
    SECURITY("Security");

    private final String value;

    AioTestCaseType(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}
