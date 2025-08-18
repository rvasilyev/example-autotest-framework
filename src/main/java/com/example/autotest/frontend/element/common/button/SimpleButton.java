package com.example.autotest.frontend.element.common.button;

import org.openqa.selenium.By;
import com.example.autotest.frontend.AbstractRegularWebElement;
import com.example.autotest.frontend.element.WebElementType;

public class SimpleButton extends AbstractRegularWebElement implements Button {

    public SimpleButton(By locator, String name) {
        super(locator, WebElementType.BUTTON, name);
    }
}
