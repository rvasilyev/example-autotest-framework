package com.example.autotest.frontend.element.common.textfield;

import com.example.autotest.frontend.element.container.WebElementContainer;
import org.openqa.selenium.By;
import com.example.autotest.frontend.element.common.label.Label;

import java.util.Objects;

public class LabelledTextField<T extends Label> extends SimpleTextField implements WebElementContainer {

    private final T label;

    public LabelledTextField(By locator, String name, T label) {
        super(locator, name);
        this.label = label;
    }

    public T getLabel() {
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
        LabelledTextField<?> that = (LabelledTextField<?>) o;

        return Objects.equals(label, that.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), label);
    }
}
