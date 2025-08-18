package com.example.autotest.frontend.element.common.checkbox;

import org.openqa.selenium.By;
import com.example.autotest.frontend.AbstractRegularWebElement;
import com.example.autotest.frontend.element.RequiredWebElement;
import com.example.autotest.frontend.element.WebElementType;

public class SimpleCheckbox extends AbstractRegularWebElement implements Checkbox, RequiredWebElement {

    public SimpleCheckbox(By locator, String name) {
        super(locator, WebElementType.CHECKBOX, name);
    }
}
