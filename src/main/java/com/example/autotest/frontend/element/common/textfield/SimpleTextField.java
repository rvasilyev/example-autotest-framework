package com.example.autotest.frontend.element.common.textfield;

import org.openqa.selenium.By;
import com.example.autotest.frontend.AbstractRegularWebElement;
import com.example.autotest.frontend.element.WebElementType;

public class SimpleTextField extends AbstractRegularWebElement implements TextField {

    public SimpleTextField(By locator, String name) {
        super(locator, WebElementType.TEXT_FIELD, name);
    }
}
