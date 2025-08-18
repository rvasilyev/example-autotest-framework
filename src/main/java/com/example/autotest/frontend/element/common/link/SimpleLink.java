package com.example.autotest.frontend.element.common.link;

import org.openqa.selenium.By;
import com.example.autotest.frontend.AbstractRegularWebElement;
import com.example.autotest.frontend.element.WebElementType;

public class SimpleLink extends AbstractRegularWebElement implements Link {

    public SimpleLink(By locator, String name) {
        super(locator, WebElementType.LINK, name);
    }
}
