package com.example.autotest.test.demo;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.example.autotest.backend.rest.auth.AuthService;
import com.example.autotest.backend.rest.jira.JiraService;
import com.example.autotest.backend.rest.jira.aio.AioService;
import com.example.autotest.backend.rest.jira.aio.dto.AioTagDto;
import com.example.autotest.backend.rest.jira.aio.type.AioTestCaseType;
import com.example.autotest.base.AbstractTests;
import com.example.autotest.base.CommonRestSteps;
import com.example.autotest.test.annotation.DependsOn;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Issue;
import io.qameta.allure.Issues;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import com.example.autotest.test.TestTags;
import com.example.autotest.test.annotation.AioData;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Tags({
        @Tag(TestTags.TestType.REGRESS),
        @Tag(TestTags.Target.BACKEND),
        @Tag(TestTags.Target.REST_API)
})
@Owner("Tester")
@Severity(SeverityLevel.NORMAL)
@Epic("API")
@Feature("Rest")
@Issues({
        @Issue("Issue-01"),
        @Issue("Issue-02")
})
class DemoTests extends AbstractTests {

    private final ElasticsearchClient elasticSearchClient;
    private final JiraService jiraService;
    private final AioService aioService;

    @Autowired
    public DemoTests(ElasticsearchClient elasticSearchClient, JiraService jiraService, AioService aioService) {
        this.elasticSearchClient = elasticSearchClient;
        this.jiraService = jiraService;
        this.aioService = aioService;
    }

    @Test
    @AioData(projectKey = "EXAMPLE", testCaseType = AioTestCaseType.FUNCTIONAL)
    @DisplayName("Демонстрационный тест 1")
    @Description("Тест демонстрирует применение кода фреймворка.")
    @Story("Демонстрация обычного теста")
    void firstExampleTest() {
        String token = CommonRestSteps.getAuthToken(new AuthService(), "login", "password");
        HttpHeaders headers = jiraService.getProjectsController().getProject("EXAMPLE", null).getHeaders();
        DemoSteps.searchElasticLogs(elasticSearchClient, 200, null);
        Assertions.assertThat(headers)
                .as("Проверка загаловков ответа")
                .doesNotContainValue(Collections.singletonList(token));
    }

    @ParameterizedTest
    @MethodSource("getTestArguments")
    @DependsOn("firstExampleTest()")
    @AioData(projectKey = "EXAMPLE", testCaseType = AioTestCaseType.FUNCTIONAL)
    @DisplayName("Демонстрационный тест 2")
    @Description("Тест демонстрирует применение кода фреймворка.")
    @Story("Демонстрация параметризованного и зависимого теста")
    @Issues({
            @Issue("Issue-03"),
            @Issue("Issue-04")
    })
    void secondExampleTest(String projectId, int responseCode) {
        ResponseEntity<List<AioTagDto>> response = aioService.getConfigurationController().getProjectTags(projectId);
        CommonRestSteps.checkResponseAndGetBody(response, responseCode);
    }

    private static Stream<Arguments> getTestArguments() {
        return Stream.of(
                Arguments.of("EXAMPLE", 200),
                Arguments.of("NULL", 400)
        );
    }
}
