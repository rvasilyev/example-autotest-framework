package com.example.autotest.test.extension;

import com.example.autotest.backend.rest.jira.JiraService;
import com.example.autotest.backend.rest.jira.aio.AioService;
import com.example.autotest.backend.rest.jira.aio.type.AioTestScriptType;
import com.example.autotest.backend.rest.jira.aio.type.AioTestStatusType;
import com.example.autotest.backend.rest.jira.dto.JiraUserAssignableToProjectsSearchParametersDto;
import com.example.autotest.test.annotation.AioData;
import io.qameta.allure.Description;
import io.qameta.allure.Issue;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.junitplatform.AllureJunitPlatform;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.util.AnnotationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import com.example.autotest.backend.rest.jira.aio.dto.AioConfigurationDto;
import com.example.autotest.backend.rest.jira.aio.dto.AioFolderDto;
import com.example.autotest.backend.rest.jira.aio.dto.AioRequirementBulkDto;
import com.example.autotest.backend.rest.jira.aio.dto.AioStepDto;
import com.example.autotest.backend.rest.jira.aio.dto.AioTagDto;
import com.example.autotest.backend.rest.jira.aio.dto.AioTagWrapperDto;
import com.example.autotest.backend.rest.jira.aio.dto.AioTestCaseDetailDto;
import com.example.autotest.backend.rest.jira.aio.dto.AioTestCaseDto;
import com.example.autotest.configuration.settings.JiraSettings;
import com.example.autotest.exception.AutotestException;
import com.example.autotest.support.ExtensionUtils;
import com.example.autotest.support.backend.RestUtils;
import com.example.autotest.test.aspect.AioStepAspect;

import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Component
@Lazy
public final class AioExtension implements AfterTestExecutionCallback {

    private final JiraService jiraService;
    private final AioService aioService;
    private final Set<String> createdTestCases;

    @Autowired
    public AioExtension(JiraService jiraService, AioService aioService) {
        this.jiraService = jiraService;
        this.aioService = aioService;
        this.createdTestCases = new HashSet<>();
    }

    @Override
    public synchronized void afterTestExecution(ExtensionContext context) {
        Runnable runnable = () -> {
            Optional<AioData> aioDataAnnotationOpt = AnnotationUtils.findAnnotation(context.getRequiredTestMethod(), AioData.class);
            if (JiraSettings.AIO_CASE_CREATION && aioDataAnnotationOpt.isPresent()) {
                context.getExecutionException()
                        .ifPresent(throwable -> {
                            throw createAioException("\n" + throwable.getMessage()).get();
                        });
                AioData aioDataAnnotation = aioDataAnnotationOpt.get();

                String projectKey = aioDataAnnotation.projectKey();
                String jiraProjectId = RestUtils.checkResponseAndGetBody(jiraService.getProjectsController().getProject(projectKey, null))
                        .getId();

                String testName = AnnotationUtils.findAnnotation(context.getRequiredTestMethod(), DisplayName.class)
                        .map(DisplayName::value)
                        .orElse(context.getDisplayName());
                String testCaseId = getTestCaseId(jiraProjectId, testName);

                if (testCaseId.isEmpty() || (JiraSettings.FORCE_AIO_CASE_CREATION && !createdTestCases.contains(testName))) {
                    AioTestCaseDetailDto detail = getDetail(context, aioDataAnnotation, projectKey, jiraProjectId, testName);
                    List<AioStepDto> steps = getSteps();

                    AioTestCaseDto aioTestCaseDto = new AioTestCaseDto().setJiraProjectId(Integer.valueOf(jiraProjectId))
                            .setDetail(detail)
                            .setSteps(steps);

                    if (!testCaseId.isEmpty()) {
                        aioService.getCaseOperationsController().updateTestCase(jiraProjectId, testCaseId, aioTestCaseDto);
                    } else {
                        aioService.getCaseOperationsController().createTestCase(jiraProjectId, true, aioTestCaseDto);
                        testCaseId = getTestCaseId(jiraProjectId, testName);
                    }
                    updateRequirements(testCaseId, jiraProjectId, context);

                    createdTestCases.add(testName);
                }
            }
        };
        ExtensionUtils.processFixture(AllureJunitPlatform.TEAR_DOWN, runnable, context);
    }

    private void updateRequirements(String testCaseId, String jiraProjectId, ExtensionContext context) {
        Function<AnnotatedElement, List<String>> collectingIdsFunction = annotatedElement -> AnnotationUtils.findRepeatableAnnotations(annotatedElement, Issue.class)
                .stream()
                .map(issue -> RestUtils.checkResponseAndGetBody(jiraService.getIssuesController().getIssue(issue.value(), null))
                        .getId()
                )
                .collect(Collectors.toList());
        List<String> requirementIds = new ArrayList<>();
        requirementIds.addAll(collectingIdsFunction.apply(context.getRequiredTestMethod()));
        requirementIds.addAll(collectingIdsFunction.apply(context.getRequiredTestClass()));

        RestUtils.checkResponseAndGetBody(aioService.getCaseOperationsController().getTestCase(jiraProjectId, testCaseId, false))
                .getDetail()
                .getRequirements()
                .forEach(aioRequirementDto -> RestUtils.checkResponse(aioService.getCaseOperationsController().removeTestCaseRequirement(jiraProjectId, testCaseId, aioRequirementDto.getRequirementId().toString())));

        AioRequirementBulkDto body = new AioRequirementBulkDto()
                .setTestCaseId(Integer.valueOf(testCaseId))
                .setIdList(requirementIds);
        RestUtils.checkResponse(aioService.getCaseOperationsController().addTestCaseRequirements(jiraProjectId, body));
    }

    private List<AioStepDto> getSteps() {
        List<AioStepDto> steps = AioStepAspect.getSteps();
        steps.forEach(step -> step.setStepOrder(steps.indexOf(step)));
        return steps;
    }

    private AioTestCaseDetailDto getDetail(ExtensionContext context, AioData aioDataAnnotation, String projectKey, String jiraProjectId, String testName) {
        AioService.ConfigurationController aioConfigurationController = aioService.getConfigurationController();
        AioConfigurationDto aioConfiguration = RestUtils.checkResponseAndGetBody(aioConfigurationController.getProjectConfig(jiraProjectId));

        return new AioTestCaseDetailDto().setAutomationKey(getAutomationKey(context))
                .setTitle(testName)
                .setDescription(getDescription(context))
                .setAutomationOwnerId(getAutomationOwnerId(projectKey, context))
                .setFolder(getFolder(aioDataAnnotation))
                .setTags(getTags(context, jiraProjectId, aioConfigurationController))
                .setTestAutomationStatusId(getTestAutomationStatusId(aioDataAnnotation, aioConfiguration))
                .setTestPriorityId(getTestPriorityId(aioConfiguration, context))
                .setTestScriptTypeId(getTestScriptTypeId(aioConfiguration))
                .setTestTypeId(getTestTypeId(aioDataAnnotation, aioConfiguration))
                .setTestStatusId(getTestStatusId(aioConfiguration));
    }

    private String getTestCaseId(String jiraProjectId, String testName) {
        return RestUtils.checkResponseAndGetBody(aioService.getCaseListingController().getTestCases(jiraProjectId))
                .stream()
                .filter(testCase -> testCase.getDetail().getTitle().equals(testName))
                .findFirst()
                .map(testCase -> String.valueOf(testCase.getId()))
                .orElse("");
    }

    private String getDescription(ExtensionContext context) {
        return AnnotationUtils.findAnnotation(context.getRequiredTestMethod(), Description.class)
                .map(Description::value)
                .orElseThrow(createAioException("отсутствуют данные @Description"));
    }

    private String getAutomationKey(ExtensionContext context) {
        String testClassName = context.getRequiredTestClass().getName();
        String testMethodName = context.getRequiredTestMethod().getName();
        return String.format("%s#%s", testClassName, testMethodName);
    }

    private Integer getTestStatusId(AioConfigurationDto aioConfiguration) {
        return aioConfiguration.getTestStatus()
                .stream()
                .filter(testStatus -> testStatus.getTestStatusType().equals(AioTestStatusType.PUBLISHED))
                .findFirst()
                .orElseThrow(createAioException("не найден статус AIO-теста 'PUBLISHED'"))
                .getId();
    }

    private Integer getTestTypeId(AioData aioDataAnnotation, AioConfigurationDto aioConfiguration) {
        return aioConfiguration.getTestType()
                .stream()
                .filter(testType -> testType.getName().equals(aioDataAnnotation.testCaseType()))
                .findFirst()
                .orElseThrow(createAioException("отсутствуют данные @AioData.testCaseType()"))
                .getId();
    }

    private Integer getTestScriptTypeId(AioConfigurationDto aioConfiguration) {
        return aioConfiguration.getTestScriptType()
                .stream()
                .filter(scriptType -> scriptType.getTestScriptType().equals(AioTestScriptType.CLASSIC))
                .findFirst()
                .orElseThrow(createAioException("не найден тип AIO-теста 'CLASSIC'"))
                .getId();
    }

    private Integer getTestPriorityId(AioConfigurationDto aioConfiguration, ExtensionContext context) {
        String severity = AnnotationUtils.findAnnotation(context.getRequiredTestMethod(), Severity.class)
                .or(() -> AnnotationUtils.findAnnotation(context.getRequiredTestClass(), Severity.class))
                .map(annotation -> annotation.value().value())
                .orElseThrow(createAioException("отсутствуют данные @Severity"));

        return aioConfiguration.getTestPriority()
                .stream()
                .filter(status -> status.getName().equalsIgnoreCase(severity))
                .findFirst()
                .orElseThrow(createAioException(String.format("не найден приоритет '%s' из @Severity", severity)))
                .getId();
    }

    private Integer getTestAutomationStatusId(AioData aioDataAnnotation, AioConfigurationDto aioConfiguration) {
        return aioConfiguration.getTestAutomationStatus()
                .stream()
                .filter(status -> status.getTestAutomationStatusType().equals(aioDataAnnotation.automationStatus()))
                .findFirst()
                .orElseThrow(createAioException("отсутствуют данные @AioData.testAutomationStatus()"))
                .getId();
    }

    private List<AioTagWrapperDto> getTags(ExtensionContext context, String jiraProjectId, AioService.ConfigurationController aioConfigurationController) {
        Set<String> tags = context.getTags();

        List<AioTagDto> existingTags = RestUtils.checkResponseAndGetBody(aioConfigurationController.getProjectTags(jiraProjectId));
        List<String> existingTagNames = existingTags.stream()
                .map(AioTagDto::getName)
                .toList();

        List<String> missingTags = tags.stream()
                .filter(tag -> !existingTagNames.contains(tag))
                .toList();
        if (missingTags.isEmpty()) {
            return existingTags.stream()
                    .filter(tag -> tags.contains(tag.getName()))
                    .map(tag -> new AioTagWrapperDto().setTag(tag))
                    .collect(Collectors.toList());
        } else {
            throw createAioException(String.format("в проекте с id %s не найдены теги %s", jiraProjectId, missingTags)).get();
        }
    }

    private AioFolderDto getFolder(AioData aioDataAnnotation) {
        return Optional.ofNullable(aioDataAnnotation.folder()).map(name -> {
            if (!name.isBlank()) {
                return new AioFolderDto().setName(name);
            } else {
                return null;
            }
        }).orElse(null);
    }

    private String getAutomationOwnerId(String projectKey, ExtensionContext context) {
        String owner = AnnotationUtils.findAnnotation(context.getRequiredTestMethod(), Owner.class)
                .or(() -> AnnotationUtils.findAnnotation(context.getRequiredTestClass(), Owner.class))
                .map(Owner::value)
                .orElseThrow(createAioException("отсутствуют данные @Owner"));

        return RestUtils.checkResponseAndGetBody(
                        jiraService.getUserSearchController()
                                .findUsersAssignableToProjects(new JiraUserAssignableToProjectsSearchParametersDto()
                                        .setProjectKeys(projectKey))
                )
                .stream()
                .filter(user -> user.getDisplayName().equals(owner))
                .findFirst()
                .orElseThrow(createAioException(String.format("не найден пользователь '%s' из @Owner", owner)))
                .getKey();
    }

    private Supplier<AutotestException> createAioException(String errorDetail) {
        return () -> new AutotestException("Невозможно сгенерировать AIO-тест: " + errorDetail);
    }
}
