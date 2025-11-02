package com.example.autotest.frontend.element.common.dropbox;

import com.example.autotest.exception.AutotestException;
import com.example.autotest.frontend.element.container.OptionContainer;
import com.example.autotest.frontend.element.ClickableWebElement;
import com.example.autotest.frontend.element.LockableWebElement;
import com.example.autotest.frontend.element.SelectableWebElement;
import com.example.autotest.frontend.element.common.option.SelectableOption;
import com.example.autotest.support.frontend.WebElementUtils;

import java.util.List;
import java.util.stream.Collectors;

public interface Dropbox<T extends SelectableOption> extends OptionContainer<T>, ClickableWebElement, LockableWebElement {

    default T getSelectedOption() {
        List<T> selectedComponents = getSelectedOptions();
        int selectedComponentsSize = selectedComponents.size();
        if (selectedComponentsSize == 1) {
            return getSelectedOptions().get(0);
        } else if (selectedComponentsSize == 0) {
            throw new AutotestException(String.format("В списке '%s' нет выбранных опций", this));
        } else {
            throw new AutotestException(String.format("Список '%s' содержит %d выбранных опций", this, selectedComponentsSize));
        }
    }

    default T getSelectedOption(String text, boolean mustEqual) {
        return WebElementUtils.getComponent(text, mustEqual, getSelectedOptions());
    }

    default List<T> getSelectedOptions() {
        return getOptions().stream()
                .filter(SelectableWebElement::isSelected)
                .collect(Collectors.toList());
    }

    default List<T> getValidOptions() {
        return getOptions().stream()
                .filter(SelectableOption::isValid)
                .collect(Collectors.toList());
    }

    default boolean hasSelectedOptions() {
        return !getSelectedOptions().isEmpty();
    }
}
