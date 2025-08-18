package com.example.autotest.support;

import com.example.autotest.exception.AutotestException;

import java.io.BufferedReader;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.stream.Collectors;

public final class FileUtils {

    private FileUtils() {
        ExceptionUtils.throwInstantiationException(getClass());
    }

    public static String getResourceContent(String relativePath) {
        try {
            URI scriptUri = Optional.ofNullable(FileUtils.class.getResource(relativePath))
                    .orElseThrow(() -> new AutotestException("Не найден файл " + relativePath))
                    .toURI();
            try (BufferedReader br = Files.newBufferedReader(Path.of(scriptUri))) {
                return br.lines().collect(Collectors.joining());
            }
        } catch (Exception e) {
            throw new AutotestException(e);
        }
    }
}
