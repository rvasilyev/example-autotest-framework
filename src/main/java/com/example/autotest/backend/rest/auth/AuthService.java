package com.example.autotest.backend.rest.auth;

import com.example.autotest.backend.rest.AbstractRestService;
import com.example.autotest.backend.rest.ExampleHeaderMap;
import com.example.autotest.backend.rest.auth.dto.GetObjectHeadersDto;
import com.example.autotest.configuration.settings.ApplicationSettings;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.RequestInterceptor;
import io.qameta.allure.Step;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.autotest.backend.rest.FeignIgnoredParam;
import com.example.autotest.support.AioUtils;
import com.example.autotest.test.annotation.AioIgnoredParam;
import com.example.autotest.test.annotation.AioStep;

import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("unused")
public class AuthService extends AbstractRestService {

    private final AuthController authController;

    public AuthService() {
        this(null);
    }

    public AuthService(String authHeaderValue) {
        super(
                "auth",
                ApplicationSettings.ENVIRONMENT_URL.replace("${service.name}", "auth"),
                new MappingJackson2HttpMessageConverter(new ObjectMapper()
                        .findAndRegisterModules()
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                        .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"))),
                Optional.ofNullable(authHeaderValue == null || authHeaderValue.isEmpty() ? null : authHeaderValue)
                        .map(value -> (RequestInterceptor) template -> template.header("Authorization", value))
                        .orElse(null)
        );
        authController = createController(AuthController.class);
    }

    public AuthController getGpbController() {
        return authController;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        AuthService that = (AuthService) o;

        return Objects.equals(authController, that.authController);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), authController);
    }

    @FeignClient(value = "authController")
    public interface AuthController {

        @AioStep(description = "Получить токен авторизации" + AioUtils.DEFAULT_REST_STEP_DESCRIPTION_SUFFIX)
        @Step("Получение токена авторизации")
        @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
        ResponseEntity<OAuth2AccessToken> getToken(@RequestBody @Validated Map<String, Object> body,
                                                   @FeignIgnoredParam @AioIgnoredParam String expectedResult);

        @AioStep(description = "Получить объект" + AioUtils.DEFAULT_REST_STEP_DESCRIPTION_SUFFIX)
        @Step("Получение объекта")
        @GetMapping("/{uid}")
        ResponseEntity<Object> getObject(@ExampleHeaderMap GetObjectHeadersDto headers,
                                         @PathVariable("uid") String uid,
                                         @FeignIgnoredParam @AioIgnoredParam String expectedResult);
    }
}
