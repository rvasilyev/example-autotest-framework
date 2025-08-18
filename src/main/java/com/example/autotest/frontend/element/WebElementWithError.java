package com.example.autotest.frontend.element;

import com.example.autotest.support.frontend.WebElementUtils;

public interface WebElementWithError extends RegularWebElement {

    default boolean hasError() {
        return WebElementUtils.wrapElementHandling(this, self -> Boolean.parseBoolean(self.getAttribute("error")));
    }
}
