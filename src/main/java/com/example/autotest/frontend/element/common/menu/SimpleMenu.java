package com.example.autotest.frontend.element.common.menu;

import com.codeborne.selenide.SelenideElement;
import com.example.autotest.frontend.element.common.option.Option;
import com.example.autotest.frontend.element.container.DynamicWebElementsContainer;
import org.openqa.selenium.By;
import com.example.autotest.frontend.element.ClickableWebElement;
import com.example.autotest.frontend.element.WebElementType;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class SimpleMenu<T extends Option> extends DynamicWebElementsContainer<T> implements Menu<T>, ClickableWebElement {

    public SimpleMenu(By locator, String name, By componentBaseLocator, BiFunction<Map.Entry<By, SelenideElement>, Integer, T> componentCreator) {
        super(locator, WebElementType.MENU, name, componentBaseLocator, componentCreator);
    }

    @Override
    public List<T> getOptions() {
        return getComponents();
    }
}
