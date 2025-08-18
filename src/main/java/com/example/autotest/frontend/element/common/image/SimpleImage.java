package com.example.autotest.frontend.element.common.image;

import org.openqa.selenium.By;
import com.example.autotest.frontend.AbstractRegularWebElement;
import com.example.autotest.frontend.element.WebElementType;

public class SimpleImage extends AbstractRegularWebElement implements Image {

    public SimpleImage(By locator, String name) {
        super(locator, WebElementType.IMAGE, name);
    }
}
