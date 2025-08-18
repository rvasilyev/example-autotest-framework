package com.example.autotest.frontend.element.common.table;

import com.codeborne.selenide.SelenideElement;
import com.example.autotest.frontend.element.container.DynamicWebElementsContainer;
import org.openqa.selenium.By;
import com.example.autotest.frontend.element.WebElementType;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class SimpleTableHeader<T extends Row<?>> extends DynamicWebElementsContainer<T> implements TableHeader<T> {

    public SimpleTableHeader(By locator, By componentBaseLocator, BiFunction<Map.Entry<By, SelenideElement>, Integer, T> componentCreator) {
        super(locator, WebElementType.HEADER, "", componentBaseLocator, componentCreator);
    }

    @Override
    public List<T> getRows() {
        return getComponents();
    }
}
