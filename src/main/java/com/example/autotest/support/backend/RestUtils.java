package com.example.autotest.support.backend;

import com.example.autotest.exception.AutotestException;
import org.springframework.http.ResponseEntity;
import com.example.autotest.support.ExceptionUtils;

public final class RestUtils {

    private RestUtils() {
        ExceptionUtils.throwInstantiationException(getClass());
    }

    public static <T> T checkResponseAndGetBody(ResponseEntity<T> responseEntity) {
        if (!responseEntity.getStatusCode().isError() && responseEntity.hasBody()) {
            return responseEntity.getBody();
        } else {
            throw new AutotestException(
                    String.format("Валидация HTTP-ответа не пройдена: код ответа %d, тело ответа %s.",
                            responseEntity.getStatusCode().value(),
                            responseEntity.getBody())
            );
        }
    }

    public static void checkResponse(ResponseEntity<?> responseEntity) {
        if (responseEntity.getStatusCode().isError()) {
            throw new AutotestException(
                    String.format("Валидация HTTP-ответа не пройдена: код ответа %d.", responseEntity.getStatusCode().value())
            );
        }
    }
}
