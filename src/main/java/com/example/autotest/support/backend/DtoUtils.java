package com.example.autotest.support.backend;

import com.example.autotest.support.ExceptionUtils;

public final class DtoUtils {

    private DtoUtils() {
        ExceptionUtils.throwInstantiationException(getClass());
    }

    public static String extractParametersAsString(String dtoString) {
        return dtoString.substring(dtoString.indexOf("{") + 1, dtoString.lastIndexOf("}")).trim();
    }
}
