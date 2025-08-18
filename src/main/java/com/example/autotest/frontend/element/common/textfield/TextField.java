package com.example.autotest.frontend.element.common.textfield;

import com.codeborne.selenide.SelenideElement;
import com.example.autotest.frontend.element.EditableWebElement;
import com.example.autotest.frontend.element.RequiredWebElement;
import com.example.autotest.frontend.element.WebElementWithText;
import com.example.autotest.support.frontend.WebElementUtils;

public interface TextField extends WebElementWithText, EditableWebElement, RequiredWebElement {

    @Override
    default String getOwnText() {
        return WebElementUtils.wrapElementHandling(this, SelenideElement::getValue);
    }
}
