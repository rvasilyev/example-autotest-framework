package com.example.autotest.frontend;

import com.example.autotest.exception.AutotestException;
import com.example.autotest.frontend.element.CustomWebElement;
import com.example.autotest.frontend.element.IndexedWebElement;
import com.example.autotest.frontend.element.WebElementType;
import org.openqa.selenium.By;
import com.example.autotest.support.frontend.WebElementUtils;

import java.util.Objects;
import java.util.regex.Pattern;

public abstract class AbstractWebElement implements CustomWebElement {

    private final By locator;
    private String name;

    protected AbstractWebElement(By locator, WebElementType webElementType, String name, Integer index) {
        if (!(locator instanceof By.ByXPath)) {
            throw new AutotestException("Локатор должен представлять собой выражение xpath");
        }

        if (this instanceof IndexedWebElement) {
            if (index == null) {
                throw new AutotestException("Отсутствует индекс для индексируемого элемента");
            } else if (index <= 0) {
                throw new AutotestException("Индексируемые элементы должны нумероваться от 1");
            }

            String locatorExpression = WebElementUtils.getLocatorExpression(locator);
            this.locator = By.xpath(String.format("(%s)[%d]", locatorExpression, index));
        } else {
            if (index != null) {
                throw new AutotestException("Присутствует индекс для неиндексируемого элемента");
            }
            this.locator = locator;
        }

        String nameRef = name.trim();
        if (!nameRef.isEmpty()) {
            Pattern pattern = Pattern.compile("^([a-z])+", Pattern.CANON_EQ);
            if (nameRef.matches(pattern.toString())) {
                String firstChar = String.valueOf(nameRef.charAt(0));
                nameRef = nameRef.replaceFirst(firstChar, firstChar.toUpperCase());
            }
            nameRef = String.format(" '%s'", nameRef);
        }
        this.name = String.format("<%s%s>", webElementType.getTypeName(), nameRef);
    }

    protected AbstractWebElement(By locator, WebElementType webElementType, int index) {
        this(locator, webElementType, String.valueOf(index), index);
    }

    protected AbstractWebElement(By locator, WebElementType webElementType, String name) {
        this(locator, webElementType, name, null);
    }

    @Override
    public By getLocator() {
        return locator;
    }

    @Override
    public String getLocatorAsString() {
        return WebElementUtils.getLocatorExpression(locator);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractWebElement that = (AbstractWebElement) o;

        return Objects.equals(locator, that.locator)
                && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locator, name);
    }

    @Override
    public String toString() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }
}
