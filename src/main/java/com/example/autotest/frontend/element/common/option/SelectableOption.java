package com.example.autotest.frontend.element.common.option;

import org.openqa.selenium.By;
import com.example.autotest.frontend.element.SelectableWebElement;

public class SelectableOption extends SimpleOption implements SelectableWebElement {

    public SelectableOption(By locator, int index) {
        super(locator, index);
    }
}
