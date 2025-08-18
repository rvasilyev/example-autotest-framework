package com.example.autotest.frontend.element.common;

import com.codeborne.selenide.SelenideElement;
import com.example.autotest.frontend.element.WebElementType;
import com.example.autotest.frontend.element.common.button.SelectableButton;
import com.example.autotest.frontend.element.container.DynamicWebElementsContainer;
import com.example.autotest.test.annotation.AioStep;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.autotest.support.frontend.WebElementUtils;

import java.util.Map;
import java.util.function.BiFunction;

public class Swiper<T extends SelectableButton> extends DynamicWebElementsContainer<T> {

    private static final Logger LOG = LoggerFactory.getLogger(Swiper.class);

    public Swiper(By locator, String name, By componentBaseLocator, BiFunction<Map.Entry<By, SelenideElement>, Integer, T> componentCreator) {
        super(locator, WebElementType.SWIPER, name, componentBaseLocator, componentCreator);
    }

    @AioStep(description = "Прокрутить {this} на {width}px")
    @Step("Прокрутка {this}")
    public void swipe(int width, String expectedResult) {
        WebElementUtils.scrollElement(this, width, false);

        LOG.info("Элемент {} прокручен на {}px", this, width);
    }

    @AioStep(description = "Прокрутить {this} до {targetElement}", expectedResult = "{this} успешно прокручен, видно {targetElement}")
    @Step("Прокрутка {this}")
    public void swipe(T targetElement) {
        WebElementUtils.scrollElement(this, getComponents(), targetElement, false);
        LOG.info("Элемент {} прокручен до элемента {}", this, targetElement);
    }
}
