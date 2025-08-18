package com.example.autotest.frontend.element.common.table;

import com.codeborne.selenide.SelenideElement;
import com.example.autotest.frontend.element.container.DynamicWebElementsContainer;
import org.openqa.selenium.By;
import com.example.autotest.frontend.element.WebElementType;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class SimpleTableBody<T extends Row<?>> extends DynamicWebElementsContainer<T> implements TableBody<T> {

    public SimpleTableBody(By locator, By componentBaseLocator, BiFunction<Map.Entry<By, SelenideElement>, Integer, T> componentCreator) {
        super(locator, WebElementType.BODY, "", componentBaseLocator, componentCreator);
    }

    @Override
    public List<T> getRows() {
        return getComponents();
    }
}
