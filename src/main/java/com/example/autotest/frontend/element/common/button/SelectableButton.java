package com.example.autotest.frontend.element.common.button;

import org.openqa.selenium.By;
import com.example.autotest.frontend.element.SelectableWebElement;

public class SelectableButton extends SimpleButton implements SelectableWebElement {

    public SelectableButton(By locator, String name) {
        super(locator, name);
    }
}
