package com.example.autotest.frontend.element.common.link;

import com.example.autotest.exception.AutotestException;
import com.example.autotest.frontend.element.ClickableWebElement;
import com.example.autotest.frontend.element.WebElementWithText;
import com.example.autotest.support.frontend.WebElementUtils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

public interface Link extends ClickableWebElement, WebElementWithText {

    default boolean hasUrl() {
        return WebElementUtils.wrapElementHandling(this, self -> self.getAttribute("href") != null);
    }

    default URL getUrl() {
        try {
            return new URI(
                    WebElementUtils.wrapElementHandling(this, self ->
                            Objects.requireNonNull(self.getAttribute("href"),
                                    String.format("У элемента '%s' отсутствует атрибут 'href'", this)))
            ).toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            throw new AutotestException(e);
        }
    }
}
