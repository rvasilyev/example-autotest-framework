package com.example.autotest.backend.rest.jira.aio;

import com.example.autotest.backend.rest.jira.aio.dto.AioConfigurationDto;
import com.example.autotest.backend.rest.jira.aio.dto.AioRequirementBulkDto;
import com.example.autotest.backend.rest.jira.aio.dto.AioTagDto;
import com.example.autotest.backend.rest.jira.aio.dto.AioTestCaseDto;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import feign.Headers;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.autotest.backend.rest.AbstractRestService;
import com.example.autotest.configuration.settings.JiraSettings;

import java.util.List;
import java.util.Objects;

/**
 * REST API для взаимодействия с Jira AIO.
 * <p>
 * <b> Реализован в минимально необходимом объеме. </b>
 * </p>
 */
@SuppressWarnings("unused")
@Component
@Lazy
public final class AioService extends AbstractRestService {

    private final CaseOperationsController caseOperationsController;
    private final CaseListingController caseListingController;
    private final ConfigurationController configurationController;

    public AioService() {
        super("aio",
                JiraSettings.JIRA_API_URL,
                new MappingJackson2HttpMessageConverter(new ObjectMapper()
                        .findAndRegisterModules()
                        .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)),
                new BasicAuthRequestInterceptor(JiraSettings.JIRA_LOGIN, JiraSettings.JIRA_PASSWORD)
        );
        caseOperationsController = createController(CaseOperationsController.class);
        caseListingController = createController(CaseListingController.class);
        configurationController = createController(ConfigurationController.class);
    }

    public CaseOperationsController getCaseOperationsController() {
        return caseOperationsController;
    }

    public CaseListingController getCaseListingController() {
        return caseListingController;
    }

    public ConfigurationController getConfigurationController() {
        return configurationController;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AioService that = (AioService) o;

        return caseOperationsController.equals(that.caseOperationsController)
                && caseListingController.equals(that.caseListingController)
                && configurationController.equals(that.configurationController);
    }

    @Override
    public int hashCode() {
        return Objects.hash(caseOperationsController, caseListingController, configurationController);
    }

    @FeignClient(value = "aioCaseOperationsController", path = "/aio-tcms/1.0/project")
    @Headers("Content-Type: application/json")
    public interface CaseOperationsController {

        @GetMapping("/{jiraProjectId}/testcase/{testCaseId}")
        ResponseEntity<AioTestCaseDto> getTestCase(@PathVariable("jiraProjectId") String jiraProjectId,
                                                   @PathVariable("testCaseId") String testCaseId,
                                                   @RequestParam(name = "needDataInRTF", required = false) Boolean needDataInRtf);

        @PostMapping("/{jiraProjectId}/testcase")
        ResponseEntity<AioTestCaseDto> createTestCase(@PathVariable("jiraProjectId") String jiraProjectId,
                                                      @RequestParam(name = "uniqueAutoKey", required = false) Boolean uniqueAutoKey,
                                                      @RequestBody @Validated AioTestCaseDto body);

        @PutMapping("/{jiraProjectId}/testcase/{testCaseId}")
        ResponseEntity<AioTestCaseDto> updateTestCase(@PathVariable("jiraProjectId") String jiraProjectId,
                                                      @PathVariable("testCaseId") String testCaseId,
                                                      @RequestBody @Validated AioTestCaseDto body);

        @DeleteMapping("/{jiraProjectId}/testcase/{testCaseId}")
        ResponseEntity<Object> deleteTestCase(@PathVariable("jiraProjectId") String jiraProjectId,
                                              @PathVariable("testCaseId") String testCaseId);

        @PutMapping("/{jiraProjectId}/traceability/testcase/bulk/requirement")
        ResponseEntity<Object> addTestCaseRequirements(@PathVariable("jiraProjectId") String jiraProjectId,
                                                      @RequestBody @Validated AioRequirementBulkDto body);

        @DeleteMapping("/{jiraProjectId}/traceability/testcase/{testCaseId}/requirement/{requirementId}")
        ResponseEntity<Void> removeTestCaseRequirement(@PathVariable("jiraProjectId") String jiraProjectId,
                                                       @PathVariable("testCaseId") String testCaseId,
                                                       @PathVariable("requirementId") String requirementId);
    }

    @FeignClient(value = "aioCaseListingController", path = "/aio-tcms/1.0/project")
    @Headers("Content-Type: application/json")
    public interface CaseListingController {

        @GetMapping("/{jiraProjectId}/testcase")
        ResponseEntity<List<AioTestCaseDto>> getTestCases(@PathVariable("jiraProjectId") String jiraProjectId);
    }

    @FeignClient(value = "aioConfigurationController", path = "/aio-tcms/1.0/project")
    @Headers("Content-Type: application/json")
    public interface ConfigurationController {

        @GetMapping("/{jiraProjectId}/config")
        ResponseEntity<AioConfigurationDto> getProjectConfig(@PathVariable("jiraProjectId") String jiraProjectId);

        @GetMapping("/{jiraProjectId}/tag")
        ResponseEntity<List<AioTagDto>> getProjectTags(@PathVariable("jiraProjectId") String jiraProjectId);
    }
}
