package com.example.autotest.frontend.element;

import org.openqa.selenium.Rectangle;

public interface RegularWebElement extends CustomWebElement {

    boolean isExist();

    boolean isDisplayed();

    void hover(String expectedResult);

    void scrollTo(boolean isOnTop);

    Rectangle getRect();
}
