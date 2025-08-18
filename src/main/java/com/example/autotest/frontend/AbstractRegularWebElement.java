package com.example.autotest.frontend;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import com.example.autotest.frontend.element.RegularWebElement;
import com.example.autotest.frontend.element.WebElementType;
import com.example.autotest.test.annotation.AioStep;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.autotest.support.frontend.WebElementUtils;

public abstract class AbstractRegularWebElement extends AbstractWebElement implements RegularWebElement {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractRegularWebElement.class);

    protected AbstractRegularWebElement(By locator, WebElementType webElementType, String name, Integer index) {
       super(locator, webElementType, name, index);
    }

    protected AbstractRegularWebElement(By locator, WebElementType webElementType, int index) {
        this(locator, webElementType, String.valueOf(index), index);
    }

    protected AbstractRegularWebElement(By locator, WebElementType webElementType, String name) {
        this(locator, webElementType, name, null);
    }

    @Override
    public boolean isExist() {
        return getSelf().exists();
    }

    @Override
    public boolean isDisplayed() {
        return WebElementUtils.wrapElementHandling(this, SelenideElement::isDisplayed);
    }

    @AioStep(description = "Навести курсор на {this}")
    @Step("Наведение курсора на {this}")
    @Override
    public void hover(String expectedResult) {
        WebElementUtils.wrapElementHandling(this, SelenideElement::hover);
        LOG.info("Указатель наведен на элемент {}", this);
    }

    @AioStep(description = "Прокрутить страницу до {this}", expectedResult = "Страница успешно прокручена, видно {this}")
    @Step("Прокрутка страницы до {this}")
    @Override
    public void scrollTo(boolean isOnTop) {
        WebElementUtils.wrapElementHandling(this, selfRef -> selfRef.scrollIntoView(isOnTop));
        LOG.info("Страница браузера прокручена до элемента {}, элемент {} страницы", this, isOnTop ? "вверху" : "внизу");
    }

    @Override
    public Rectangle getRect() {
        return WebElementUtils.wrapElementHandling(this, SelenideElement::getRect);
    }

    protected SelenideElement getSelf() {
        return Selenide.element(getLocator());
    }
}
