package com.example.autotest.backend.rest.jira.aio.dto;

import com.example.autotest.backend.rest.jira.aio.type.AioTestStepType;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AioStepDto implements Serializable {

    private Integer id;
    private Integer stepOrder;
    private String step;
    private String testData;
    private String expectedResult;
    private AioTestStepType testStepType;
    private AioTestCaseDto referencedTest;
    private String bddStep;
    private List<AioAttachmentDto> attachments = Collections.emptyList();
    private List<AioAttachmentDto> stepAttachments = Collections.emptyList();
    private List<AioAttachmentDto> dataAttachments = Collections.emptyList();
    private List<AioAttachmentDto> expectedResultAttachments = Collections.emptyList();

    @JsonProperty("ID")
    public Integer getId() {
        return id;
    }

    @JsonProperty("ID")
    public AioStepDto setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getStepOrder() {
        return stepOrder;
    }

    public AioStepDto setStepOrder(Integer stepOrder) {
        this.stepOrder = stepOrder;
        return this;
    }

    public String getStep() {
        return step;
    }

    public AioStepDto setStep(String step) {
        this.step = step;
        return this;
    }

    public String getTestData() {
        return testData;
    }

    public AioStepDto setTestData(String testData) {
        this.testData = testData;
        return this;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public AioStepDto setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
        return this;
    }

    public AioTestStepType getTestStepType() {
        return testStepType;
    }

    public AioStepDto setTestStepType(AioTestStepType testStepType) {
        this.testStepType = testStepType;
        return this;
    }

    public AioTestCaseDto getReferencedTest() {
        return referencedTest;
    }

    public AioStepDto setReferencedTest(AioTestCaseDto referencedTest) {
        this.referencedTest = referencedTest;
        return this;
    }

    public String getBddStep() {
        return bddStep;
    }

    public AioStepDto setBddStep(String bddStep) {
        this.bddStep = bddStep;
        return this;
    }

    public List<AioAttachmentDto> getAttachments() {
        return attachments;
    }

    public AioStepDto setAttachments(List<AioAttachmentDto> attachments) {
        this.attachments = attachments;
        return this;
    }

    public List<AioAttachmentDto> getStepAttachments() {
        return stepAttachments;
    }

    public AioStepDto setStepAttachments(List<AioAttachmentDto> stepAttachments) {
        this.stepAttachments = stepAttachments;
        return this;
    }

    public List<AioAttachmentDto> getDataAttachments() {
        return dataAttachments;
    }

    public AioStepDto setDataAttachments(List<AioAttachmentDto> dataAttachments) {
        this.dataAttachments = dataAttachments;
        return this;
    }

    public List<AioAttachmentDto> getExpectedResultAttachments() {
        return expectedResultAttachments;
    }

    public AioStepDto setExpectedResultAttachments(List<AioAttachmentDto> expectedResultAttachments) {
        this.expectedResultAttachments = expectedResultAttachments;
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
        AioStepDto that = (AioStepDto) o;

        return Objects.equals(id, that.id)
                && Objects.equals(stepOrder, that.stepOrder)
                && Objects.equals(step, that.step)
                && Objects.equals(testData, that.testData)
                && Objects.equals(expectedResult, that.expectedResult)
                && testStepType == that.testStepType
                && Objects.equals(referencedTest, that.referencedTest)
                && Objects.equals(bddStep, that.bddStep)
                && Objects.equals(attachments, that.attachments)
                && Objects.equals(stepAttachments, that.stepAttachments)
                && Objects.equals(dataAttachments, that.dataAttachments)
                && Objects.equals(expectedResultAttachments, that.expectedResultAttachments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                id,
                stepOrder,
                step,
                testData,
                expectedResult,
                testStepType,
                referencedTest,
                bddStep,
                attachments,
                stepAttachments,
                dataAttachments,
                expectedResultAttachments
        );
    }

    @Override
    public String toString() {
        return String.format(
                "AioStepDto {%n id=%s,%n stepOrder=%s,%n step=%s,%n testData=%s,%n expectedResult=%s,%n testStepType=%s,%n referencedTest=%s,%n bddStep=%s,%n attachments=%s,%n stepAttachments=%s,%n dataAttachments=%s,%n expectedResultAttachments=%s%n}",
                id,
                stepOrder,
                step,
                testData,
                expectedResult,
                testStepType,
                referencedTest,
                bddStep,
                attachments,
                stepAttachments,
                dataAttachments,
                expectedResultAttachments
        );
    }
}
