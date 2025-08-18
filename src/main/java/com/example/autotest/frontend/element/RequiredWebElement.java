package com.example.autotest.frontend.element;

import com.example.autotest.support.frontend.WebElementUtils;

public interface RequiredWebElement extends RegularWebElement {

    default boolean isRequired() {
        return WebElementUtils.wrapElementHandling(this, self -> Boolean.parseBoolean(self.getAttribute("required")));
    }
}
