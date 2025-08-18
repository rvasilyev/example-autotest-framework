package com.example.autotest.frontend.element.common.spoiler;

import com.example.autotest.frontend.element.container.HeaderAndBodyContainer;
import org.openqa.selenium.By;
import com.example.autotest.frontend.element.WebElementType;

public class SimpleSpoiler<T extends SimpleSpoilerHeader, S extends SpoilerBody> extends HeaderAndBodyContainer<T, S> implements Spoiler<T, S> {

    public SimpleSpoiler(By locator, int index, T header, S body) {
        super(locator, WebElementType.SPOILER, index, header, body);
    }
}
