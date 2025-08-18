package com.example.autotest.frontend.element.common.label;

import org.openqa.selenium.By;
import com.example.autotest.frontend.AbstractRegularWebElement;
import com.example.autotest.frontend.element.WebElementType;

public class SimpleLabel extends AbstractRegularWebElement implements Label {

    public SimpleLabel(By locator, String name) {
        super(locator, WebElementType.LABEL, name);
    }
}
