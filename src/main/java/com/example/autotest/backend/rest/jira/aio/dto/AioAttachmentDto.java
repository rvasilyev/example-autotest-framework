package com.example.autotest.backend.rest.jira.aio.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class AioAttachmentDto {

    private Integer id;
    private String name;
    private String storeName;
    private Object storeLocation;
    private String mimeType;
    private Long size;
    private String processedSize;
    private String checksum;
    private String ownerId;
    private Long createdDate;
    private Integer projectId;

    @JsonProperty("ID")
    public Integer getId() {
        return id;
    }

    @JsonProperty("ID")
    public AioAttachmentDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public AioAttachmentDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getStoreName() {
        return storeName;
    }

    public AioAttachmentDto setStoreName(String storeName) {
        this.storeName = storeName;
        return this;
    }

    public Object getStoreLocation() {
        return storeLocation;
    }

    public AioAttachmentDto setStoreLocation(Object storeLocation) {
        this.storeLocation = storeLocation;
        return this;
    }

    public String getMimeType() {
        return mimeType;
    }

    public AioAttachmentDto setMimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public Long getSize() {
        return size;
    }

    public AioAttachmentDto setSize(Long size) {
        this.size = size;
        return this;
    }

    public String getProcessedSize() {
        return processedSize;
    }

    public AioAttachmentDto setProcessedSize(String processedSize) {
        this.processedSize = processedSize;
        return this;
    }

    public String getChecksum() {
        return checksum;
    }

    public AioAttachmentDto setChecksum(String checksum) {
        this.checksum = checksum;
        return this;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public AioAttachmentDto setOwnerId(String ownerId) {
        this.ownerId = ownerId;
        return this;
    }

    public Long getCreatedDate() {
        return createdDate;
    }

    public AioAttachmentDto setCreatedDate(Long createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public AioAttachmentDto setProjectId(Integer projectId) {
        this.projectId = projectId;
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
        AioAttachmentDto that = (AioAttachmentDto) o;

        return Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(storeName, that.storeName)
                && Objects.equals(storeLocation, that.storeLocation)
                && Objects.equals(mimeType, that.mimeType)
                && Objects.equals(size, that.size)
                && Objects.equals(processedSize, that.processedSize)
                && Objects.equals(checksum, that.checksum)
                && Objects.equals(ownerId, that.ownerId)
                && Objects.equals(createdDate, that.createdDate)
                && Objects.equals(projectId, that.projectId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                name,
                storeName,
                storeLocation,
                mimeType,
                size,
                processedSize,
                checksum,
                ownerId,
                createdDate,
                projectId
        );
    }

    @Override
    public String toString() {
        return String.format(
                "AioAttachmentDto {%n id=%s,%n name=%s,%n storeName=%s,%n storeLocation=%s,%n mimeType=%s,%n size=%s,%n processedSize=%s,%n checksum=%s,%n ownerId=%s,%n createdDate=%s,%n projectId=%s%n}",
                id,
                name,
                storeName,
                storeLocation,
                mimeType,
                size,
                processedSize,
                checksum,
                ownerId,
                createdDate,
                projectId
        );
    }
}
