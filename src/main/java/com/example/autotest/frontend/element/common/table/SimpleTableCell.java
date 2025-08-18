package com.example.autotest.frontend.element.common.table;

import org.openqa.selenium.By;
import com.example.autotest.frontend.AbstractRegularWebElement;
import com.example.autotest.frontend.element.WebElementType;
import com.example.autotest.frontend.element.WebElementWithText;

public class SimpleTableCell extends AbstractRegularWebElement implements Cell, WebElementWithText {

    public SimpleTableCell(By locator, int index) {
        super(locator, WebElementType.CELL, index);
    }
}
