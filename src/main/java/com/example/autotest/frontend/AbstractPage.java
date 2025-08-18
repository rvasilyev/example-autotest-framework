package com.example.autotest.frontend;

import com.example.autotest.frontend.element.WebElementType;
import com.example.autotest.frontend.element.common.Loader;
import com.example.autotest.frontend.element.container.WebElementContainer;
import org.openqa.selenium.By;

import java.util.Objects;

public abstract class AbstractPage extends AbstractWebElement implements WebElementContainer {

    private final Loader loader;

    protected AbstractPage(By locator, String name, By loaderLocator) {
        super(locator, WebElementType.PAGE, name);
        this.loader = new Loader(loaderLocator);
    }

    public Loader getLoader() {
        return loader;
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
        AbstractPage that = (AbstractPage) o;

        return Objects.equals(loader, that.loader);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), loader);
    }
}
