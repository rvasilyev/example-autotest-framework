package com.example.autotest.frontend.element.common;

import org.openqa.selenium.By;
import com.example.autotest.frontend.AbstractRegularWebElement;
import com.example.autotest.frontend.element.LockableWebElement;
import com.example.autotest.frontend.element.WebElementType;

public class Loader extends AbstractRegularWebElement implements LockableWebElement {

    public Loader(By locator) {
        super(locator, WebElementType.LOADER, "");
    }
}
