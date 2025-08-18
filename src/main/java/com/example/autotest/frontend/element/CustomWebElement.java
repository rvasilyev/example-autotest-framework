package com.example.autotest.frontend.element;

import org.openqa.selenium.By;

public interface CustomWebElement {

    By getLocator();

    String getLocatorAsString();
}
