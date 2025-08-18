package com.example.autotest.frontend.element.container;

import com.example.autotest.support.frontend.WebElementUtils;
import com.example.autotest.frontend.element.common.option.Option;

import java.util.List;

public interface OptionContainer<T extends Option> extends WebElementContainer {

    List<T> getOptions();

    default T getOption(String text, boolean mustEqual) {
        return WebElementUtils.getComponent(text, mustEqual, getOptions());
    }

    default boolean hasOption(String optionText, boolean mustEqual) {
        return WebElementUtils.hasComponent(optionText, mustEqual, getOptions());
    }

    default boolean hasOptions() {
        return !getOptions().isEmpty();
    }
}
