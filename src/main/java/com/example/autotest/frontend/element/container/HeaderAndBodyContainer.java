package com.example.autotest.frontend.element.container;

import org.openqa.selenium.By;
import com.example.autotest.frontend.AbstractRegularWebElement;
import com.example.autotest.frontend.element.RegularWebElement;
import com.example.autotest.frontend.element.WebElementType;

import java.util.Objects;

public abstract class HeaderAndBodyContainer<T extends RegularWebElement, S extends RegularWebElement> extends AbstractRegularWebElement implements WebElementContainer {

    private final T header;
    private final S body;

    protected HeaderAndBodyContainer(By locator, WebElementType webElementType, String name, Integer index, T header, S body) {
        super(locator, webElementType, name, index);
        this.header = header;
        this.body = body;
    }

    protected HeaderAndBodyContainer(By locator, WebElementType webElementType, String name, T header, S body) {
        this(locator, webElementType, name, null, header, body);
    }

    protected HeaderAndBodyContainer(By locator, WebElementType webElementType, Integer index, T header, S body) {
        this(locator, webElementType, String.valueOf(index), index, header, body);
    }

    public T getHeader() {
        return header;
    }

    public S getBody() {
        return body;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        HeaderAndBodyContainer<?, ?> that = (HeaderAndBodyContainer<?, ?>) o;

        return Objects.equals(header, that.header) && Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), header, body);
    }
}
