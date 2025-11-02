package com.example.autotest.backend.rest.jira.aio.dto;

import java.util.Objects;

public class AioTagWrapperDto {

    private AioTagDto tag;

    public AioTagDto getTag() {
        return tag;
    }

    public AioTagWrapperDto setTag(AioTagDto tag) {
        this.tag = tag;
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
        AioTagWrapperDto that = (AioTagWrapperDto) o;

        return Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag);
    }

    @Override
    public String toString() {
        return String.format("AioTagWrapperDto {%n tag=%s%n}", tag);
    }
}
