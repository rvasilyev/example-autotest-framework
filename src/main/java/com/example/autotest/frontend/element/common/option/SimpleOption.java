package com.example.autotest.frontend.element.common.option;

import org.openqa.selenium.By;
import com.example.autotest.frontend.AbstractRegularWebElement;
import com.example.autotest.frontend.element.WebElementType;

public class SimpleOption extends AbstractRegularWebElement implements Option {

    public SimpleOption(By locator, int index) {
        super(locator, WebElementType.OPTION, index);
    }

    public boolean isValid() {
        return !getText().isBlank();
    }
}
