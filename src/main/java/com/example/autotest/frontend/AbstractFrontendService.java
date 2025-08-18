package com.example.autotest.frontend;

import com.codeborne.selenide.Selenide;
import com.example.autotest.configuration.settings.ApplicationSettings;

import java.util.Objects;

public abstract class AbstractFrontendService {

    private final String name;
    private final String url;

    protected AbstractFrontendService(String name) {
        this.name = name;
        this.url = ApplicationSettings.ENVIRONMENT_URL.replace("${service.name}", name);
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public void open() {
        Selenide.open(url);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractFrontendService that = (AbstractFrontendService) o;

        return Objects.equals(name, that.name) && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, url);
    }
}
