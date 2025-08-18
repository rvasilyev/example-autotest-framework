package com.example.autotest.frontend.element.common.dropbox;

import com.codeborne.selenide.SelenideElement;
import com.example.autotest.frontend.element.container.DynamicWebElementsContainer;
import org.openqa.selenium.By;
import com.example.autotest.frontend.element.RequiredWebElement;
import com.example.autotest.frontend.element.WebElementType;
import com.example.autotest.frontend.element.common.option.SelectableOption;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

public class SimpleDropbox<T extends SelectableOption> extends DynamicWebElementsContainer<T> implements Dropbox<T>, RequiredWebElement {

    public SimpleDropbox(By locator, String name, By componentBaseLocator, BiFunction<Map.Entry<By, SelenideElement>, Integer, T> componentCreator) {
        super(locator, WebElementType.DROPBOX, name, componentBaseLocator, componentCreator);
    }

    @Override
    public List<T> getOptions() {
        return getComponents();
    }
}
