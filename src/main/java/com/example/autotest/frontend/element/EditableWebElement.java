package com.example.autotest.frontend.element;

import com.example.autotest.test.annotation.AioStep;
import io.qameta.allure.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.autotest.support.frontend.WebElementUtils;

public interface EditableWebElement extends ClickableWebElement, LockableWebElement {

    @AioStep(description = "Вести значение в {this}")
    @Step("Ввод значения в {this}")
    default void sendKeys(CharSequence keysToSend, String expectedResult) {
        WebElementUtils.wrapElementHandling(this, self -> {
            self.sendKeys(keysToSend);
            return null;
        });
        log().info("В элемент {} введено значение '{}'", this, keysToSend);
    }

    default void sendKeys(CharSequence keysToSend) {
        sendKeys(keysToSend, "Значение в {this} успешно введено");
    }

    @AioStep(description = "Очистить значение в {this}", expectedResult = "Значение в {this} успешно очищено")
    @Step("Очистка значения в {this}")
    default void clear() {
        WebElementUtils.wrapElementHandling(this, self -> {
            self.clear();
            return null;
        });
        log().info("Очищено значение в элементе {}", this);
    }

    private Logger log() {
        return LoggerFactory.getLogger(EditableWebElement.class);
    }
}
