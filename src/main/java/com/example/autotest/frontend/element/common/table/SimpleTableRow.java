package com.example.autotest.frontend.element.common.table;

import com.codeborne.selenide.SelenideElement;
import com.example.autotest.frontend.element.container.DynamicWebElementsContainer;
import org.openqa.selenium.By;
import com.example.autotest.frontend.element.WebElementType;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class SimpleTableRow<T extends Cell> extends DynamicWebElementsContainer<T> implements Row<T> {

    public SimpleTableRow(By locator, int index, By componentBaseLocator, BiFunction<Map.Entry<By, SelenideElement>, Integer, T> componentCreator) {
        super(locator, WebElementType.ROW, index, componentBaseLocator, componentCreator);
    }

    @Override
    public List<T> getCells() {
        return getComponents();
    }
}
