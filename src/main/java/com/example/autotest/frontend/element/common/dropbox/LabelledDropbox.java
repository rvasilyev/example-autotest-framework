package com.example.autotest.frontend.element.common.dropbox;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import com.example.autotest.frontend.element.common.label.Label;
import com.example.autotest.frontend.element.common.option.SelectableOption;

import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;

public class LabelledDropbox<T extends SelectableOption, S extends Label> extends SimpleDropbox<T> {

    private final S label;

    public LabelledDropbox(By locator, String name, By componentBaseLocator, BiFunction<Map.Entry<By, SelenideElement>, Integer, T> componentCreator, S label) {
        super(locator, name, componentBaseLocator, componentCreator);
        this.label = label;
    }

    public S getLabel() {
        return label;
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
        LabelledDropbox<?, ?> that = (LabelledDropbox<?, ?>) o;

        return Objects.equals(label, that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), label);
    }
}
