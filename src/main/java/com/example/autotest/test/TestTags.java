package com.example.autotest.test;

import com.example.autotest.support.ExceptionUtils;

/**
 * Набор групп строковых констант для использования с аннотацией {@link org.junit.jupiter.api.Tag}.
 */
public final class TestTags {

    private TestTags() {
        ExceptionUtils.throwInstantiationException(getClass());
    }

    /**
     * Набот тэгов по виду тестирования.
     */
    public static final class TestType {

        public static final String SMOKE = "smoke";
        public static final String REGRESS = "regress";

        private TestType() {
            ExceptionUtils.throwInstantiationException(getClass());
        }
    }

    /**
     * Набор тэгов по тестируемым частям системы.
     */
    public static final class Target {

        public static final String FRONTEND = "frontend";
        public static final String BACKEND = "backend";
        public static final String REST_API = "rest-api";
        public static final String DATABASE = "database";

        private Target() {
            ExceptionUtils.throwInstantiationException(getClass());
        }
    }
}
