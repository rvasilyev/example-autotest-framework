package com.example.autotest.backend.rest.jira.aio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public class AioTagDto implements Serializable {

    private Integer id;
    private String name;

    @JsonProperty("ID")
    public Integer getId() {
        return id;
    }

    @JsonProperty("ID")
    public AioTagDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public AioTagDto setName(String name) {
        this.name = name;
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
        AioTagDto aioTagDto = (AioTagDto) o;

        return Objects.equals(id, aioTagDto.id) && Objects.equals(name, aioTagDto.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return String.format("AioTagDto {%n id=%s,%n name=%s%n}", id, name);
    }
}
