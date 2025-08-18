package com.example.autotest.frontend.element;

import com.codeborne.selenide.SelenideElement;
import com.example.autotest.support.frontend.WebElementUtils;

public interface WebElementWithText extends RegularWebElement {

    default String getText() {
        return WebElementUtils.wrapElementHandling(this, SelenideElement::getText);
    }

    default String getOwnText() {
        return WebElementUtils.wrapElementHandling(this, SelenideElement::getOwnText);
    }

    default String getInnerText() {
        return WebElementUtils.wrapElementHandling(this, SelenideElement::innerText);
    }
}
