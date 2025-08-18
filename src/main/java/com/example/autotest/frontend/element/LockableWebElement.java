package com.example.autotest.frontend.element;

import com.example.autotest.support.frontend.WebElementUtils;

import java.util.Optional;

public interface LockableWebElement extends RegularWebElement {

    default boolean isEnabled() {
        return WebElementUtils.wrapElementHandling(this, self -> self.isEnabled()
                && !Optional.ofNullable(self.getAttribute("class")).orElse("").contains("disabled"));
    }
}
