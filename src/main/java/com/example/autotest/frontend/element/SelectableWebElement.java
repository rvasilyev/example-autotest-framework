package com.example.autotest.frontend.element;

import com.codeborne.selenide.SelenideElement;
import com.example.autotest.support.frontend.WebElementUtils;

import java.util.Optional;

public interface SelectableWebElement extends RegularWebElement {

    default boolean isSelected() {
        return WebElementUtils.wrapElementHandling(this, self -> {
            SelenideElement parent = self.parent();
            return self.isSelected() || Boolean.parseBoolean(self.getAttribute("checked"))
                    || Boolean.parseBoolean(self.getAttribute("selected"))
                    || Boolean.parseBoolean(self.getAttribute("aria-selected"))
                    || Boolean.parseBoolean(self.getAttribute("data-checked"))
                    || Boolean.parseBoolean(parent.getAttribute("data-checked"))
                    || Boolean.parseBoolean(parent.getAttribute("data-selected"))
                    || Optional.ofNullable(self.getAttribute("class")).orElse("").contains("active");
        });
    }
}
