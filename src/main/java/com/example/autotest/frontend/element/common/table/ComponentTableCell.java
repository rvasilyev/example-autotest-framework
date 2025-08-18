package com.example.autotest.frontend.element.common.table;

import com.example.autotest.frontend.element.container.WebElementContainer;
import org.openqa.selenium.By;
import com.example.autotest.frontend.AbstractRegularWebElement;
import com.example.autotest.frontend.element.RegularWebElement;
import com.example.autotest.frontend.element.WebElementType;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class ComponentTableCell extends AbstractRegularWebElement implements Cell, WebElementContainer {

    private final List<RegularWebElement> components;

    public ComponentTableCell(By locator, int index, Function<String, List<RegularWebElement>> componentMapper) {
        super(locator, WebElementType.CELL, index);
        this.components = componentMapper.apply(getLocatorAsString());
    }

    public List<RegularWebElement> getComponents() {
        return components;
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
        ComponentTableCell that = (ComponentTableCell) o;

        return Objects.equals(components, that.components);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), components);
    }
}
