package com.example.autotest.backend.rest.jira.aio.dto;

import java.util.Objects;

/**
 * Представление прав доступа к сущности.
 * <ul>
 *     Возможные значения кодов доступа:
 *     <li>7 - полный доступ (удаление, запись, чтение)</li>
 *     <li>3 - только запись и чтение</li>
 *     <li>1 - только чтение</li>
 *     <li>0 - нет доступа</li>
 * </ul>
 */
public class AioPermissionDto {

    private Integer value;
    private String error;

    public Integer getValue() {
        return value;
    }

    public AioPermissionDto setValue(Integer value) {
        this.value = value;
        return this;
    }

    public String getError() {
        return error;
    }

    public AioPermissionDto setError(String error) {
        this.error = error;
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
        AioPermissionDto that = (AioPermissionDto) o;

        return Objects.equals(value, that.value) && Objects.equals(error, that.error);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, error);
    }

    @Override
    public String toString() {
        return String.format("AioPermissionDto {%n value=%s,%n error=%s%n}", value, error);
    }
}
