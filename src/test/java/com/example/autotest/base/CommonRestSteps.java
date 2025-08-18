package com.example.autotest.base;

import com.example.autotest.backend.rest.auth.AuthService;
import io.qameta.allure.Param;
import io.qameta.allure.Step;
import io.qameta.allure.model.Parameter;
import org.assertj.core.api.Assertions;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import com.example.autotest.support.AioUtils;
import com.example.autotest.support.ExceptionUtils;

import java.util.Map;

public final class CommonRestSteps {

    private CommonRestSteps() {
        ExceptionUtils.throwInstantiationException(getClass());
    }

    @Step("Получение и проверка токена авторизации")
    public static String getAuthToken(@Param(mode = Parameter.Mode.HIDDEN) AuthService authService,
                                      String login,
                                      @Param(mode = Parameter.Mode.MASKED) String password) {

        ResponseEntity<OAuth2AccessToken> getTokenResponse = authService.getGpbController()
                .getToken(
                        Map.of("login", "login", "password", "password"),
                        AioUtils.getDefaultRestStepExpectedResult(200, "org.springframework.security.oauth2.core.OAuth2AccessToken(org.springframework.security.oauth2.core.OAuth2AccessToken.TokenType(java.lang.String()),java.lang.String(),java.time.Instant#now(),java.time.Instant#now())")
                );
        OAuth2AccessToken token = checkResponseAndGetBody(getTokenResponse, 200);

        return String.format("%s %s", token.getTokenType(), token.getTokenValue());
    }

    @Step("Проверка полученного ответа")
    public static <T> T checkResponseAndGetBody(@Param(mode = Parameter.Mode.HIDDEN) ResponseEntity<T> responseEntity,
                                                int responseCode) {
        Assertions.assertThat(responseEntity.getStatusCode().value())
                .describedAs("Неверный код ответа")
                .isEqualTo(responseCode);
        Assertions.assertThat(responseEntity.hasBody())
                .describedAs("Отсутствует тело ответа")
                .isTrue();
        return responseEntity.getBody();
    }
}
