package com.example.autotest.backend.rest.auth.dto;

import org.springframework.util.MimeType;

import java.util.Objects;

@SuppressWarnings("unused")
public class GetObjectHeadersDto {

    private MimeType mimeType;
    private Integer maxSize;

    public MimeType getMimeType() {
        return mimeType;
    }

    public GetObjectHeadersDto setMimeType(MimeType mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public GetObjectHeadersDto setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
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
        GetObjectHeadersDto that = (GetObjectHeadersDto) o;

        return mimeType.equals(that.mimeType) && Objects.equals(maxSize, that.maxSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mimeType, maxSize);
    }

    @Override
    public String toString() {
        return String.format("GetObjectHeadersDto {%n mimeType=%s,%n maxSize=%s%n}", mimeType, maxSize);
    }
}
