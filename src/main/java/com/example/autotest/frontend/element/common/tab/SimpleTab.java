package com.example.autotest.frontend.element.common.tab;

import com.example.autotest.frontend.element.container.HeaderAndBodyContainer;
import org.openqa.selenium.By;
import com.example.autotest.frontend.element.WebElementType;

public class SimpleTab<T extends SimpleTabHeader, S extends TabBody> extends HeaderAndBodyContainer<T, S> implements Tab<T, S> {

    public SimpleTab(By locator, int index, T header, S body) {
        super(locator, WebElementType.TAB, index, header, body);
    }
}
