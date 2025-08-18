package com.example.autotest.frontend.element;

import com.example.autotest.test.annotation.AioStep;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.autotest.support.frontend.WebElementUtils;

public interface ClickableWebElement extends RegularWebElement {

    @AioStep(description = "Нажать на {this}")
    @Step("Нажатие на {this}")
    default void click(String expectedResult) {
        WebElementUtils.wrapElementHandling(this, self -> {
            self.click();
            return null;
        });
        log().info("Произведено нажатие на элемент {}", this);
    }

    @AioStep(description = "Вызвать контекстное меню для {this}")
    @Step("Контекстное нажатие на {this}")
    default void contextClick(String expectedResult) {
        WebElementUtils.wrapElementHandling(this, self -> {
            self.contextClick();
            return null;
        });
        log().info("Произведено контекстное нажатие на элемент {}", this);
    }

    @AioStep(description = "Дважды нажать на {this}")
    @Step("Двойное нажатие на {this}")
    default void doubleClick(String expectedResult) {
        WebElementUtils.wrapElementHandling(this, self -> {
            self.doubleClick();
            return null;
        });
        log().info("Произведено двойное нажатие на элемент {}", this);
    }

    private Logger log() {
        return LoggerFactory.getLogger(ClickableWebElement.class);
    }
}
