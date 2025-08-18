package com.example.autotest.frontend.element.common.image;

import org.openqa.selenium.By;
import com.example.autotest.frontend.element.ClickableWebElement;
import com.example.autotest.frontend.element.LockableWebElement;

public class ClickableImage extends SimpleImage implements ClickableWebElement, LockableWebElement {

    public ClickableImage(By locator, String name) {
        super(locator, name);
    }
}
