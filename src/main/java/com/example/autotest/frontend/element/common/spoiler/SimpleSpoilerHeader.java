package com.example.autotest.frontend.element.common.spoiler;

import org.openqa.selenium.By;
import com.example.autotest.frontend.AbstractRegularWebElement;
import com.example.autotest.frontend.element.WebElementType;
import com.example.autotest.frontend.element.WebElementWithText;

public class SimpleSpoilerHeader extends AbstractRegularWebElement implements SpoilerHeader, WebElementWithText {

    public SimpleSpoilerHeader(By locator) {
        super(locator, WebElementType.HEADER, "");
    }
}
