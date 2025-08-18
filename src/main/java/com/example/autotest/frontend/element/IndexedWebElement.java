package com.example.autotest.frontend.element;

public interface IndexedWebElement extends RegularWebElement {

    default int getIndex() {
        String locator = getLocatorAsString();
        String index = locator.substring(locator.lastIndexOf('[') + 1, locator.lastIndexOf(']'));
        return Integer.parseInt(index);
    }
}
