package com.example.autotest.backend.rest.jira;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Headers;
import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.autotest.backend.rest.AbstractRestService;
import com.example.autotest.backend.rest.jira.dto.JiraIssueDto;
import com.example.autotest.backend.rest.jira.dto.JiraIssueSearchParametersDto;
import com.example.autotest.backend.rest.jira.dto.JiraProjectDto;
import com.example.autotest.backend.rest.jira.dto.JiraProjectSearchParametersDto;
import com.example.autotest.backend.rest.jira.dto.JiraUserDto;
import com.example.autotest.backend.rest.jira.dto.JiraUserAssignableToProjectsSearchParametersDto;
import com.example.autotest.configuration.settings.JiraSettings;

import java.util.List;
import java.util.Objects;

/**
 * REST API для взаимодействия с Jira.
 * <p>
 * <b>Реализован в минимально необходимом объеме, включая DTO.</b>
 * </p>
 *
 * @see <a href="https://developer.atlassian.com/cloud/jira/platform/rest/v2/intro">Полная документация Jira REST API</a>
 */
@SuppressWarnings("unused")
@Component
@Lazy
public final class JiraService extends AbstractRestService {

    private final IssuesController issuesController;
    private final ProjectsController projectsController;
    private final UserSearchController userSearchController;

    public JiraService() {
        super("jira",
                JiraSettings.JIRA_API_URL,
                new MappingJackson2HttpMessageConverter(new ObjectMapper()
                        .findAndRegisterModules()
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)),
                new BasicAuthRequestInterceptor(JiraSettings.JIRA_LOGIN, JiraSettings.JIRA_PASSWORD)
        );
        issuesController = createController(IssuesController.class);
        projectsController = createController(ProjectsController.class);
        userSearchController = createController(UserSearchController.class);
    }

    public IssuesController getIssuesController() {
        return issuesController;
    }

    public ProjectsController getProjectsController() {
        return projectsController;
    }

    public UserSearchController getUserSearchController() {
        return userSearchController;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JiraService that = (JiraService) o;

        return issuesController.equals(that.issuesController)
                && projectsController.equals(that.projectsController)
                && userSearchController.equals(that.userSearchController);
    }

    @Override
    public int hashCode() {
        return Objects.hash(issuesController, projectsController, userSearchController);
    }

    @FeignClient(value = "jiraIssuesController", path = "/api/2")
    @Headers("Content-Type: application/json")
    public interface IssuesController {

        @GetMapping("/issue/{issueIdOrKey}")
        ResponseEntity<JiraIssueDto> getIssue(@PathVariable("issueIdOrKey") String issueIdOrKey,
                                              @SpringQueryMap JiraIssueSearchParametersDto params);

    }

    @FeignClient(value = "jiraProjectsController", path = "/api/2")
    @Headers("Content-Type: application/json")
    public interface ProjectsController {

        @GetMapping("/project/{projectIdOrKey}")
        ResponseEntity<JiraProjectDto> getProject(@PathVariable("projectIdOrKey") String projectIdOrKey,
                                                  @SpringQueryMap JiraProjectSearchParametersDto params);
    }

    @FeignClient(value = "jiraUserSearchController", path = "/api/2")
    @Headers("Content-Type: application/json")
    public interface UserSearchController {

        @GetMapping("/user/assignable/multiProjectSearch")
        ResponseEntity<List<JiraUserDto>> findUsersAssignableToProjects(@SpringQueryMap JiraUserAssignableToProjectsSearchParametersDto params);
    }
}
