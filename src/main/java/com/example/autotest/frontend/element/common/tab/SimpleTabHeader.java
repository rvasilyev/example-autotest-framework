package com.example.autotest.frontend.element.common.tab;

import org.openqa.selenium.By;
import com.example.autotest.frontend.AbstractRegularWebElement;
import com.example.autotest.frontend.element.WebElementType;
import com.example.autotest.frontend.element.WebElementWithText;

public class SimpleTabHeader extends AbstractRegularWebElement implements TabHeader, WebElementWithText {

    public SimpleTabHeader(By locator, int index) {
        super(locator, WebElementType.HEADER, index);
    }
}
